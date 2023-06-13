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
package org.openhab.binding.switchbothub.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link SwitchBotHubBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Franz - Initial contribution
 */
@NonNullByDefault
public class SwitchBotHubBindingConstants {

    private static final String BINDING_ID = "switchbothub";

    // List of all Thing Type UIDs
    public static final ThingTypeUID BRIDGE_TYPE_ACCOUNT = new ThingTypeUID(BINDING_ID, "account");
    public static final ThingTypeUID THING_TYPE_HUB = new ThingTypeUID(BINDING_ID, "hub");

    // List of all Channel ids
    public static final String CHANNEL_STATE = "state";

    // List of configuration parameters
    public static final String CONFIG_DEVICE_ID = "deviceId";
}
