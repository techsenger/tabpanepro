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

package com.techsenger.tabpanepro.demo.skin;

import com.techsenger.tabpanepro.core.skin.TabPaneProSkin;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractSlantedTabHeaderSkin extends TabPaneProSkin.TabHeaderSkin {

    private final Path borderPath = new Path();

    private final Path selectedPath = new Path();

    private final Polygon polygon = new Polygon();

    private final Polygon clipPolygon = new Polygon();

    public AbstractSlantedTabHeaderSkin(TabPaneProSkin.TabHeaderContext context) {
        super(context);

        borderPath.setStroke(Color.web("#48484A"));
        selectedPath.setStroke(Color.web("#0A84FF"));
        selectedPath.setStrokeWidth(2.0);
        polygon.setFill(Color.TRANSPARENT);

        borderPath.setManaged(false);
        selectedPath.setManaged(false);
        polygon.setManaged(false);

        setClip(clipPolygon);
        getChildren().addAll(0, List.of(polygon, borderPath, selectedPath));
    }

    protected Polygon getPolygon() {
        return polygon;
    }

    protected Polygon getClipPolygon() {
        return clipPolygon;
    }

    protected Path getBorderPath() {
        return borderPath;
    }

    protected Path getSelectedPath() {
        return selectedPath;
    }
}
