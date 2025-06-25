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

import java.util.List;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableBooleanProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.control.ScrollBar;

/**
 *
 * @author Pavel Castornii
 */
class TabScrollBar extends ScrollBar {

    static enum HeaderPosition {

        ABOVE_TABS,

        BELOW_TABS
    }

    private static final class Css {

        private static final CssMetaData<TabScrollBar, HeaderPosition> HEADER_POSITION;

        private static final CssMetaData<TabScrollBar, Boolean> STICK_TO_EDGE;

        private static final List<CssMetaData<?, ?>> META_DATA;

        static {
            var factory = new StyleablePropertyFactory<TabScrollBar>(ScrollBar.getClassCssMetaData());
            HEADER_POSITION = factory.createEnumCssMetaData(HeaderPosition.class, "-tpp-header-position",
                    b -> b.headerPosition, HeaderPosition.BELOW_TABS);
            STICK_TO_EDGE = factory.createBooleanCssMetaData("-tpp-stick-to-edge", b -> b.stickToEdge, false);
            META_DATA = List.copyOf(factory.getCssMetaData());
        }
    }

    public static List<CssMetaData<?, ?>> getClassCssMetaData() {
        return Css.META_DATA;
    }

    @Override
    public List<CssMetaData<?, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    private final StyleableObjectProperty<HeaderPosition> headerPosition =
            new SimpleStyleableObjectProperty(Css.HEADER_POSITION, this, "headerPosition", HeaderPosition.BELOW_TABS);

    private final StyleableBooleanProperty stickToEdge =
            new SimpleStyleableBooleanProperty(Css.STICK_TO_EDGE, this, "stickToEdge", false);

    public TabScrollBar() {
        getStyleClass().add("tab-scroll-bar");
    }

    public final StyleableObjectProperty<HeaderPosition> headerPositionProperty() {
        return headerPosition;
    }

    public final HeaderPosition getHeaderPosition() {
        return headerPosition.get();
    }

    public final void setHeaderPosition(HeaderPosition value) {
        headerPosition.set(value);
    }

    public final StyleableBooleanProperty stickToEdgeProperty() {
        return stickToEdge;
    }

    public final boolean isStickToEdge() {
        return stickToEdge.get();
    }

    public final void setStickToEdge(boolean value) {
        stickToEdge.set(value);
    }
}
