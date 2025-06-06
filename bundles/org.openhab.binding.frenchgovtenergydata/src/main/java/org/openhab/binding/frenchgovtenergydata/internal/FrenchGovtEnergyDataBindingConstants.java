/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.frenchgovtenergydata.internal;

import java.util.Currency;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link FrenchGovtEnergyDataBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class FrenchGovtEnergyDataBindingConstants {

    private static final String BINDING_ID = "frenchgovtenergydata";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_BASE = new ThingTypeUID(BINDING_ID, "base");
    public static final ThingTypeUID THING_TYPE_HPHC = new ThingTypeUID(BINDING_ID, "hphc");
    public static final ThingTypeUID THING_TYPE_TEMPO = new ThingTypeUID(BINDING_ID, "tempo");

    // List of all Channel ids
    public static final String CHANNEL_TARIFF_START = "tariff-start";
    public static final String CHANNEL_FIXED_HT = "fixed-ht";
    public static final String CHANNEL_FIXED_TTC = "fixed-ttc";
    public static final String CHANNEL_VARIABLE_HT = "variable-ht";
    public static final String CHANNEL_VARIABLE_TTC = "variable-ttc";
    public static final String CHANNEL_HC_HT = "hc-ht";
    public static final String CHANNEL_HC_TTC = "hc-ttc";
    public static final String CHANNEL_HP_HT = "hp-ht";
    public static final String CHANNEL_HP_TTC = "hp-ttc";

    public static final String CHANNEL_RED_HC_HT = "red-hc-ht";
    public static final String CHANNEL_RED_HC_TTC = "red-hc-ttc";
    public static final String CHANNEL_RED_HP_HT = "red-hp-ht";
    public static final String CHANNEL_RED_HP_TTC = "red-hp-ttc";

    public static final String CHANNEL_WHITE_HC_HT = "white-hc-ht";
    public static final String CHANNEL_WHITE_HC_TTC = "white-hc-ttc";
    public static final String CHANNEL_WHITE_HP_HT = "white-hp-ht";
    public static final String CHANNEL_WHITE_HP_TTC = "white-hp-ttc";

    public static final String CHANNEL_BLUE_HC_HT = "blue-hc-ht";
    public static final String CHANNEL_BLUE_HC_TTC = "blue-hc-ttc";
    public static final String CHANNEL_BLUE_HP_HT = "blue-hp-ht";
    public static final String CHANNEL_BLUE_HP_TTC = "blue-hp-ttc";

    public static final Currency CURRENCY_EUR = Currency.getInstance("EUR");
}
