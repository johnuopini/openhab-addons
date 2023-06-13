/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.switchbothub.internal.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.core.io.net.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Generic SwitchBot API Client.
 *
 * @author Frabz - initial contribution
 * @link <a href="https://github.com/OpenWonderLabs/SwitchBotAPI">SwitchBot API Docs</a>.
 */
public class SwitchBotAPIClient {
    private final String baseUrl;
    private final String openToken;
    private final String secretKey;

    private final Gson gson = new Gson();

    private final Logger logger = LoggerFactory.getLogger(SwitchBotAPIClient.class);

    public SwitchBotAPIClient(String baseUrl, String openToken, String secretKey) {
        this.baseUrl = baseUrl;
        this.openToken = openToken;
        this.secretKey = secretKey;
    }

    /**
     * Query the SwitchBot API
     *
     * @param endpoint The API endpoint to query, e.g. "/devices"
     * @param clazz The class to deserialize the response to
     * @return The response as a JsonObject
     * @throws IOException if the request fails
     * @throws NoSuchAlgorithmException if the HMAC algorithm is not available
     * @throws InvalidKeyException if the secret key is invalid
     */
    public <T> T query(@NonNull String endpoint, Class<T> clazz)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        JsonObject response = query(endpoint);
        return gson.fromJson(response, clazz);
    }

    /**
     * Query the SwitchBot API
     *
     * @param endpoint The API endpoint to query, e.g. "/devices"
     * @return The response as a JsonObject
     * @throws IOException if the request fails
     * @throws NoSuchAlgorithmException if the HMAC algorithm is not available
     * @throws InvalidKeyException if the secret key is invalid
     */
    public JsonObject query(@NonNull String endpoint)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        long t = System.currentTimeMillis();
        String nonce = UUID.randomUUID().toString();
        String signature = generateSignature(t, nonce);
        Properties headers = createHeaders(t, nonce, signature);
        String resultString = HttpUtil.executeUrl("GET", baseUrl + endpoint, headers, null, "application/json", 20000);
        if (resultString == null) {
            logger.error("Query: error while querying {}", endpoint);
            throw new IOException("SwitchBot API returned null");
        }
        JsonObject result = gson.fromJson(resultString, JsonObject.class);
        if (result.get("statusCode").getAsInt() != 100) {
            logger.error("Query: error while querying {}: {}", endpoint, result);
            throw new IOException("SwitchBot API returned error: " + result.get("statusCode").getAsInt());
        }
        return result;
    }

    private String generateSignature(long t, String nonce) throws NoSuchAlgorithmException, InvalidKeyException {
        String stringToSign = openToken + t + nonce;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    private Properties createHeaders(long t, String nonce, String signature) {
        Properties headers = new Properties();
        headers.setProperty("Authorization", openToken);
        headers.setProperty("t", Long.toString(t));
        headers.setProperty("sign", signature);
        headers.setProperty("nonce", nonce);
        headers.setProperty("Content-Type", "application/json");
        headers.setProperty("charset", "utf8");
        return headers;
    }
}
