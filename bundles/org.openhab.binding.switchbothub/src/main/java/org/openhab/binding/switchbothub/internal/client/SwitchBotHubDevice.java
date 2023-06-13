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

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * Generic SwitchBot API Hub Device.
 *
 * @author Frabz - initial contribution
 * @link <a href="https://github.com/OpenWonderLabs/SwitchBotAPI#devices">SwitchBot API Devices</a>.
 */
@NonNullByDefault
public class SwitchBotHubDevice extends SwitchBotDevice {

    /**
     * Determines if Cloud Service is enabled or not for the current device
     */
    @SerializedName("enableCloudService")
    public final boolean enableCloudService;

    /**
     * Device's parent Hub ID. 000000000000 when the device itself is a Hub or it is connected through Wi-Fi.
     */
    @SerializedName("hubDeviceId")
    public final String hubDeviceId;

    public SwitchBotHubDevice(String deviceId, String deviceName, String deviceType, boolean enableCloudService,
            String hubDeviceId) {
        super(deviceId, deviceName, deviceType);
        this.enableCloudService = enableCloudService;
        this.hubDeviceId = hubDeviceId;
    }
}
