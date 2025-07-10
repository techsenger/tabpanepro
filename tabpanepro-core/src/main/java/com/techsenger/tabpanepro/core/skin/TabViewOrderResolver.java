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

/**
 * A functional interface that determines the view order of tab headers in a TabHeaderArea.
 * <p>
 * Implementations of this interface are used to dynamically calculate the z-order
 * of tabs during layout operations. The resolved value will be used to set the
 * {@code viewOrder} property of tab headers.
 *
 * <p>Important Implementation Notes:
 * <ul>
 *   <li>This resolver may be called during early initialization phases when:
 *     <ul>
 *       <li>{@code Tab.isSelected()} may return false even for selected tabs</li>
 *       <li>{@code Tab.getTabPane()} may return null despite being attached</li>
 *       <li>Other tab properties may be in inconsistent states</li>
 *     </ul>
 *   </li>
 *   <li>The resolver should depend only on the provided parameters ({@code index},
 *       {@code tabCount}, {@code selected}) when possible, as these are guaranteed
 *       to be accurate.</li>
 *   <li>The {@code selected} parameter is more reliable than {@code tab.isSelected()}
 *       during initialization.</li>
 * </ul>
 * @author Pavel Castornii
 */
@FunctionalInterface
public interface TabViewOrderResolver {

    /**
     * Resolves the view order value for a specific tab.
     *
     * @param tabHeader the tab header whose view order is being resolved.
     * @param index the zero-based index position of the tab in its TabPane
     * @param tabCount the total number of tabs currently in the TabPane
     * @param selected whether the tab should be treated as selected (don't use {@code tab.isSelected()})
     */
    double resolve(TabPaneProSkin.TabHeaderSkin tabHeader, int index, int tabCount, boolean selected);
}
