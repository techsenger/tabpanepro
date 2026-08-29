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

import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Pavel Castornii
 */
public final class DragAndDropUtils {

    /**
     * Builds the drag-preview popup content {@link TabPaneProSkin.TabHeaderArea#setTabDragContentFactory} needs:
     * a snapshot of the dragged tab's own header, flipped to stay upright when the pane's tabs sit on the
     * bottom side. CSS: {@code .tab-drag-content}.
     *
     * @param tabHeader the header being dragged.
     * @return the popup content.
     */
    public static Node createTabDragContent(TabPaneProSkin.TabHeaderSkin tabHeader) {
        var tabParams = new SnapshotParameters();
        WritableImage tabImage = tabHeader.snapshot(tabParams, null);
        ImageView dragView = new ImageView(tabImage);
        var tab = tabHeader.getContext().getTab();
        if (tab.getTabPane().getSide() == Side.BOTTOM) {
            Rotate rotate = new Rotate(180, tabImage.getWidth() / 2, tabImage.getHeight() / 2);
            dragView.getTransforms().add(rotate);
        }
        var container = new VBox(dragView);
        container.getStyleClass().add("tab-drag-content");
        return container;
    }

    private DragAndDropUtils() {
        // empty
    }
}
