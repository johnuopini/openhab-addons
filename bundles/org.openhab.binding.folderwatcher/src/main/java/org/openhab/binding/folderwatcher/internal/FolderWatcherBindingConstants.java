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
package org.openhab.binding.folderwatcher.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link FolderWatcherBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Alexandr Salamatov - Initial contribution
 */
@NonNullByDefault
public class FolderWatcherBindingConstants {
    private static final String BINDING_ID = "folderwatcher";
    public static final ThingTypeUID THING_TYPE_FTPFOLDER = new ThingTypeUID(BINDING_ID, "ftpfolder");
    public static final ThingTypeUID THING_TYPE_LOCALFOLDER = new ThingTypeUID(BINDING_ID, "localfolder");
    public static final ThingTypeUID THING_TYPE_S3BUCKET = new ThingTypeUID(BINDING_ID, "s3bucket");
    public static final ThingTypeUID THING_TYPE_AZUREBLOB = new ThingTypeUID(BINDING_ID, "azureblob");
    public static final String CHANNEL_NEWFILE = "newfile";
}
