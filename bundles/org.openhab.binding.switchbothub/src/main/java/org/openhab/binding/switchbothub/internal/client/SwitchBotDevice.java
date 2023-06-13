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
 * Generic SwitchBot API Device.
 *
 * @author Frabz - initial contribution
 * @link <a href="https://github.com/OpenWonderLabs/SwitchBotAPI#devices">SwitchBot API Devices</a>.
 */
@NonNullByDefault
public abstract class SwitchBotDevice {
    @SerializedName("deviceId")
    public final String deviceId;
    @SerializedName("deviceName")
    public final String deviceName;
    @SerializedName("deviceType")
    public final String deviceType;

    public SwitchBotDevice(String deviceId, String deviceName, String deviceType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }
}
