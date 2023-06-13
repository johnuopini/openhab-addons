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
package org.openhab.binding.switchbothub.internal.discovery;

import static org.openhab.binding.switchbothub.internal.SwitchBotHubBindingConstants.CONFIG_DEVICE_ID;
import static org.openhab.binding.switchbothub.internal.SwitchBotHubBindingConstants.THING_TYPE_HUB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.switchbothub.internal.SwitchBotHubHandlerFactory;
import org.openhab.binding.switchbothub.internal.client.SwitchBotDevice;
import org.openhab.binding.switchbothub.internal.client.SwitchBotHubDevice;
import org.openhab.binding.switchbothub.internal.handler.SwitchBotHubAccountHandler;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link SwitchBotHubDiscoveryService} is responsible for starting the discovery procedure
 * connecting to SwitchBot API and discovering all devices.
 *
 * @author Franz - initial contribution
 */
public class SwitchBotHubDiscoveryService extends AbstractDiscoveryService {

    private final Logger logger = LoggerFactory.getLogger(SwitchBotHubDiscoveryService.class);

    private static final int TIMEOUT = 15;

    private final SwitchBotHubAccountHandler handler;
    private final ThingUID bridgeUID;

    private ScheduledFuture<?> scanTask;

    public SwitchBotHubDiscoveryService(SwitchBotHubAccountHandler handler) {
        super(SwitchBotHubHandlerFactory.DISCOVERABLE_THING_TYPES_UIDS, TIMEOUT);
        this.handler = handler;
        this.bridgeUID = handler.getThing().getUID();
    }

    private void getAllDevices() {
        try {
            List<SwitchBotDevice> devices = handler.queryDevices();
            for (SwitchBotDevice device : devices) {
                addThing(device);
            }
        } catch (Exception e) {
            logger.error("getAllDevices(): Error while discovering devices: {}", e.getMessage(), e);
        }
    }

    @Override
    protected void startBackgroundDiscovery() {
        getAllDevices();
    }

    @Override
    protected void startScan() {
        if (this.scanTask != null) {
            scanTask.cancel(true);
        }
        this.scanTask = scheduler.schedule(this::getAllDevices, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void stopScan() {
        super.stopScan();
        if (this.scanTask != null) {
            this.scanTask.cancel(true);
            this.scanTask = null;
        }
    }

    private void addThing(SwitchBotDevice device) {
        logger.debug("addThing(): Adding new Switch Bot device ({}) to the inbox", device.deviceName);
        Map<String, Object> properties = new HashMap<>();
        ThingUID thingUID;
        if (device instanceof SwitchBotHubDevice) {
            thingUID = new ThingUID(THING_TYPE_HUB, bridgeUID, device.deviceId);
        } else {
            logger.error("addThing(): Unknown Switch Bot device type: {}", device.deviceType);
            return;
        }
        properties.put(CONFIG_DEVICE_ID, device.deviceId);
        thingDiscovered(DiscoveryResultBuilder.create(thingUID).withLabel(device.deviceName).withBridge(bridgeUID)
                .withProperties(properties).build());
    }
}
