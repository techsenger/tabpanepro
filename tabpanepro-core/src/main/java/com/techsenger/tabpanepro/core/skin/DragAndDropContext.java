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

package com.techsenger.tabpanepro.core.skin;

import com.techsenger.tabpanepro.core.TabPanePro;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Tab;

/**
 *
 * @author Pavel Castornii
 */
public class DragAndDropContext {

    /**
     * The tab that is being dragged and dropped.
     */
    private final ReadOnlyObjectWrapper<Tab> tab = new ReadOnlyObjectWrapper<>();

    /**
     * The TabPane that is ready to accept this tab if the drop occurs at the current mouse position.
     */
    private final ReadOnlyObjectWrapper<TabPanePro> targetTabPane = new ReadOnlyObjectWrapper<>();

    public Tab getTab() {
        return this.tab.get();
    }

    public ReadOnlyObjectProperty<Tab> tabProperty() {
        return this.tab.getReadOnlyProperty();
    }

    public TabPanePro getTargetTabPane() {
        return targetTabPane.get();
    }

    public ReadOnlyObjectProperty<TabPanePro> targetTabPaneProperty() {
        return this.targetTabPane.getReadOnlyProperty();
    }

    void setTab(Tab tab) {
        this.tab.set(tab);
    }

    void setTargetTabPane(TabPanePro targetTabPane) {
        this.targetTabPane.set(targetTabPane);
    }

    void clear() {
        this.tab.set(null);
        this.targetTabPane.set(null);
    }
}
