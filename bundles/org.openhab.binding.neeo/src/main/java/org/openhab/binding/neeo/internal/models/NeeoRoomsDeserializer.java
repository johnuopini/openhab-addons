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
package org.openhab.binding.neeo.internal.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * The implementation of {@link JsonDeserializer} to deserialize a {@link NeeoRooms} class
 *
 * @author Tim Roberts - Initial contribution
 */
@NonNullByDefault
public class NeeoRoomsDeserializer implements JsonDeserializer<@Nullable NeeoRooms> {
    @Nullable
    @Override
    public NeeoRooms deserialize(@Nullable JsonElement jsonElement, @Nullable Type type,
            @Nullable JsonDeserializationContext context) throws JsonParseException {
        Objects.requireNonNull(jsonElement, "jsonElement cannot be null");
        Objects.requireNonNull(context, "context cannot be null");
        if (jsonElement instanceof JsonObject jsonObject) {
            final List<NeeoRoom> recipes = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                final NeeoRoom room = context.deserialize(entry.getValue(), NeeoRoom.class);
                recipes.add(room);
            }

            return new NeeoRooms(recipes.toArray(new NeeoRoom[0]));
        }
        return null;
    }
}
