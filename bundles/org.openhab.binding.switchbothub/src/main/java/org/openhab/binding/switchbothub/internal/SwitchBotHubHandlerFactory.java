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

import static org.openhab.binding.switchbothub.internal.SwitchBotHubBindingConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.switchbothub.internal.discovery.SwitchBotHubDiscoveryService;
import org.openhab.binding.switchbothub.internal.handler.SwitchBotHubAccountHandler;
import org.openhab.binding.switchbothub.internal.handler.SwitchBotHubHandler;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link SwitchBotHubHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Franz - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.switchbothub", service = ThingHandlerFactory.class)
public class SwitchBotHubHandlerFactory extends BaseThingHandlerFactory {

    private final Map<ThingUID, ServiceRegistration<DiscoveryService>> discoveryServiceRegistrations = new HashMap<>();

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(BRIDGE_TYPE_ACCOUNT, THING_TYPE_HUB);

    public static final Set<ThingTypeUID> DISCOVERABLE_THING_TYPES_UIDS = Set.of(THING_TYPE_HUB);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (THING_TYPE_HUB.equals(thingTypeUID)) {
            return new SwitchBotHubHandler(thing);
        } else if (BRIDGE_TYPE_ACCOUNT.equals(thingTypeUID)) {
            SwitchBotHubAccountHandler handler = new SwitchBotHubAccountHandler((Bridge) thing);
            registerAccountDiscoveryService(handler);
            return handler;
        }
        return null;
    }

    @Override
    protected void removeHandler(@NonNull ThingHandler thingHandler) {
        ServiceRegistration<DiscoveryService> serviceRegistration = discoveryServiceRegistrations
                .get(thingHandler.getThing().getUID());
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
        }
    }

    private void registerAccountDiscoveryService(SwitchBotHubAccountHandler handler) {
        SwitchBotHubDiscoveryService discoveryService = new SwitchBotHubDiscoveryService(handler);
        ServiceRegistration<DiscoveryService> serviceRegistration = this.bundleContext
                .registerService(DiscoveryService.class, discoveryService, null);
        discoveryServiceRegistrations.put(handler.getThing().getUID(), serviceRegistration);
    }
}
