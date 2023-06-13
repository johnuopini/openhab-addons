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
package org.openhab.binding.switchbothub.internal.handler;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.switchbothub.internal.SwitchBotHubConfiguration;
import org.openhab.binding.switchbothub.internal.client.SwitchBotAPIClient;
import org.openhab.binding.switchbothub.internal.client.SwitchBotDevice;
import org.openhab.binding.switchbothub.internal.client.SwitchBotHubDevice;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Bridge handler to manage SwitchBot Cloud Account.
 *
 * @link <a href="https://github.com/OpenWonderLabs/SwitchBotAPI">SwitchBot API Docs</a>.
 * @author Frabz - initial contribution
 */
@NonNullByDefault
public class SwitchBotHubAccountHandler extends BaseBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(SwitchBotHubAccountHandler.class);

    private @Nullable SwitchBotAPIClient client;

    private final Gson gson = new Gson();

    public SwitchBotHubAccountHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void initialize() {
        SwitchBotHubConfiguration config = getConfig().as(SwitchBotHubConfiguration.class);
        synchronized (this) {
            SwitchBotAPIClient client = this.client;
            if (client == null) {
                client = new SwitchBotAPIClient(config.baseUrl, config.openToken, config.secretKey);
                this.client = client;
            }
        }
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING, "Wait for login");
        checkConnection();
    }

    @Override
    public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
        super.childHandlerInitialized(childHandler, childThing);
    }

    @NonNull
    public List<SwitchBotDevice> queryDevices() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        List<SwitchBotDevice> devices = new ArrayList<>();
        if (client == null) {
            return devices;
        }
        JsonObject response = client.query("devices");
        JsonObject body = response.getAsJsonObject("body");
        JsonArray deviceList = body.getAsJsonArray("deviceList");
        for (JsonElement deviceElement : deviceList) {
            JsonObject deviceObject = deviceElement.getAsJsonObject();
            String deviceType = deviceObject.get("deviceType").getAsString();
            if ("Hub".equals(deviceType) || "Hub Plus".equals(deviceType) || "Hub Mini".equals(deviceType)
                    || "Hub 2".equals(deviceType)) {
                SwitchBotHubDevice hubDevice = gson.fromJson(deviceObject, SwitchBotHubDevice.class);
                if (hubDevice != null)
                    devices.add(hubDevice);
            } else {
                logger.info("Skipping non-Hub device: {}", deviceObject.get("deviceId").getAsString());
            }
        }
        return devices;
    }

    private void checkConnection() {
        if (client == null) {
            logger.warn("Client not initialized");
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_PENDING, "No client");
        }
        try {
            JsonObject response = client.query("devices");
            JsonObject body = response.getAsJsonObject("body");
            JsonArray deviceList = body.getAsJsonArray("deviceList");
            logger.info("API connection OK, found {} devices", deviceList.size());
            updateStatus(ThingStatus.ONLINE);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            logger.error("Error querying devices", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Error querying devices");
        }
    }
}
