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

import javafx.scene.control.Tab;

/**
 *
 * @author Pavel Castornii
 */
public class DragAndDropContext {

    /**
     * The tab that is being dragged and dropped.
     */
    private Tab tab;

    /**
     * The index of the tab.
     */
    private int tabIndex = -1;

    /**
     * The TabPane that is ready to accept this tab if the drop occurs at the current mouse position.
     */
    private TabPanePro targetTabPane;

    public Tab getTab() {
        return this.tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public TabPanePro getTargetTabPane() {
        return targetTabPane;
    }

    public void setTargetTabPane(TabPanePro targetTabPane) {
        this.targetTabPane = targetTabPane;
    }

    public void clear() {
        this.tab = null;
        this.tabIndex = -1;
        this.targetTabPane = null;
    }

}
