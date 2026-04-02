/*
 * Copyright (c) 2025 Pavel Castornii. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. This particular file is
 * subject to the "Classpath" exception as provided in the LICENSE file
 * that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.techsenger.tabpanepro.core;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Tab;

/**
 *
 * @author Pavel Castornii
 */
public class TabEvent extends Event {

    public static final EventType<TabEvent> ANY = new EventType<>(Event.ANY, "TAB_DRAG");

    public static final EventType<TabEvent> TAB_DRAG_STARTED = new EventType<>(ANY, "TAB_DRAG_STARTED");

    public static final EventType<TabEvent> TAB_DRAG_FINISHED = new EventType<>(ANY, "TAB_DRAG_FINISHED");

    public static final EventType<TabEvent> TAB_DROPPED = new EventType<>(ANY, "TAB_DROPPED");

    private final Tab tab;

    public TabEvent(EventType<TabEvent> eventType, Tab tab) {
        super(eventType);
        this.tab = tab;
    }

    /**
     * Returns the tab involved in the drag-and-drop operation.
     *
     * @return the tab being dragged or dropped
     */
    public Tab getTab() {
        return tab;
    }
}
