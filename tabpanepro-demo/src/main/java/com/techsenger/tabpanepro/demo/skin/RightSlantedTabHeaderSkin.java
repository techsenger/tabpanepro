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
import java.util.function.BiFunction;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

/**
 *
 * @author Pavel Castornii
 */
public class RightSlantedTabHeaderSkin extends AbstractSlantedTabHeaderSkin {

    private static final BiFunction<Side, Double, Double> dropOffsetResolver = (side, gap) -> {
        if (side == Side.TOP|| side == Side.RIGHT) {
            return gap;
        }
        return 0.0;
    };

    public static BiFunction<Side, Double, Double> getDropOffsetResolver() {
        return dropOffsetResolver;
    }

    private final Insets extraPadding = new Insets(2, 18, 0, 0);

    public RightSlantedTabHeaderSkin(TabPaneProSkin.TabHeaderContext context) {
        super(context);
    }

    @Override
    protected double computePrefWidth(double height) {
        return super.computePrefWidth(height) + 18;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren(extraPadding);

        double widthBottom = snapSizeX(getWidth());
        double widthTop = snapSizeX(widthBottom - 18);
        double height = snapSizeY(getHeight());

        Double[] points = {0.0, 5.0, widthTop, 5.0, widthBottom, height, 0.0, height};
        getPolygon().getPoints().clear();
        getPolygon().getPoints().addAll(points);
        getClipPolygon().getPoints().clear();
        getClipPolygon().getPoints().addAll(points);

        getBorderPath().getElements().clear();
        getBorderPath().getElements().addAll(
            new MoveTo(0.0, height),
            new LineTo(0.0, 5.0),
            new LineTo(widthTop, 5.0),
            new LineTo(widthBottom, height)
        );

        getSelectedPath().getElements().clear();
        if (getContext().getTab().isSelected()) {
            getSelectedPath().getElements().addAll(
                new MoveTo(0.0, 6.0),
                new LineTo(widthTop, 6.0)
            );
        }
    }
}