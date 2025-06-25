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

import com.techsenger.tabpanepro.core.skin.TabPaneProSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author Pavel Castornii
 */
public class TabPanePro extends TabPane {

    private final BooleanProperty headerVisibleWhenEmpty = new SimpleBooleanProperty(true);

    private final BooleanProperty tabScrollBarEnabled = new SimpleBooleanProperty(false);

    public TabPanePro(Tab... tabs) {
        super(tabs);
        setSkin(new TabPaneProSkin(this));
        getStyleClass().add("tab-pane-pro");
    }

    /**
     * Returns the property that controls whether the header is visible when no tabs are present.
     *
     * @return the property controlling header visibility when empty
     */
    public BooleanProperty headerVisibleWhenEmptyProperty() {
        return headerVisibleWhenEmpty;
    }

    /**
     * Returns whether the header is visible when there are no tabs.
     *
     * @return true if the header is visible when empty, false otherwise
     */
    public boolean isHeaderVisibleWhenEmpty() {
        return headerVisibleWhenEmpty.get();
    }

    /**
     * Sets whether the header should be visible when there are no tabs.
     *
     * @param visible true to show the header when empty, false to hide it
     */
    public void setHeaderVisibleWhenEmpty(boolean visible) {
        headerVisibleWhenEmpty.set(visible);
    }

    /**
     * Returns the property that controls whether a scroll bar is enabled
     * next to the tab headers when the tabs overflow.
     *
     * @return the tabScrollBarEnabled property
     */
    public BooleanProperty tabScrollBarEnabledProperty() {
        return tabScrollBarEnabled;
    }

    /**
     * Returns whether a scroll bar is enabled next to the tab headers
     * when the tabs overflow.
     *
     * @return {@code true} if the scroll bar is enabled, {@code false} otherwise
     */
    public boolean isTabScrollBarEnabled() {
        return tabScrollBarEnabled.get();
    }

    /**
     * Sets whether a scroll bar is enabled next to the tab headers
     * when the tabs overflow.
     *
     * @param enabled {@code true} to enable the scroll bar, {@code false} to disable it
     */
    public void setTabScrollBarEnabled(boolean enabled) {
        tabScrollBarEnabled.set(enabled);
    }
}
