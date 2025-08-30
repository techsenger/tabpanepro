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

import com.techsenger.tabpanepro.core.skin.DragAndDropContext;
import com.techsenger.tabpanepro.core.skin.TabPaneProSkin;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author Pavel Castornii
 */
public class TabPanePro extends TabPane {

    private BooleanProperty tabDragEnabled;

    private ObjectProperty<Predicate<Tab>> tabDragPredicate;

    private BooleanProperty tabDropEnabled;

    private ObjectProperty<Predicate<Tab>> tabDropPredicate;

    private ObjectProperty<DragAndDropContext> dragAndDropContext;

    private final ObservableList<Consumer<Tab>> tabDragHandlers = FXCollections.observableArrayList();

    private final ObservableList<BiConsumer<Tab, Boolean>> tabDropHandlers = FXCollections.observableArrayList();

    public TabPanePro() {
        this(null);
    }

    public TabPanePro(Tab... tabs) {
        super(tabs);
        setSkin(new TabPaneProSkin(this));
        getStyleClass().add("tab-pane-pro");
    }

    /**
     * Defines whether tab dragging is enabled.
     * <p>
     * When this property is {@code true}, users can drag tabs to reorder or move them within the tab pane.
     * When {@code false}, tab dragging is disabled and tabs remain fixed in their positions.
     * <p>
     * By default, this property is {@code false}.
     *
     * @return the property representing the tab drag-enabled state
     */
    public final BooleanProperty tabDragEnabledProperty() {
        if (this.tabDragEnabled == null) {
            this.tabDragEnabled = new SimpleBooleanProperty(this, "tabDragEnabled", false);
        }
        return tabDragEnabled;
    }

    /**
     * Returns the value of {@link #tabDragEnabledProperty()}.
     *
     * @return {@code true} if tab dragging is enabled; {@code false} otherwise
     */
    public final boolean isTabDragEnabled() {
        return this.tabDragEnabled == null ? false : tabDragEnabledProperty().get();
    }

    /**
     * Sets the value of {@link #tabDragEnabledProperty()}.
     *
     * @param enabled {@code true} to enable tab dragging; {@code false} to disable it
     */
    public final void setTabDragEnabled(boolean enabled) {
        tabDragEnabledProperty().set(enabled);
    }

    /**
     * Defines the predicate that determines whether a specific tab can be dragged from this pane.
     * <p>
     * This property holds a {@link Predicate} that is evaluated for each {@link Tab} to decide if dragging
     * is permitted. If the predicate returns {@code true}, the tab can be dragged; if {@code false}, dragging
     * is disallowed for that tab.
     * <p>
     * If the predicate is {@code null}, all tabs are considered draggable by default.
     * <p>
     * This predicate can leverage tab-specific metadata via {@link Tab#getProperties()} to customize
     * drag behavior on a per-tab basis.
     *
     * @return the property holding the drag predicate for tabs
     */
    public final ObjectProperty<Predicate<Tab>> tabDragPredicateProperty() {
        if (this.tabDragPredicate == null) {
            this.tabDragPredicate = new SimpleObjectProperty<>(this, "tabDragPredicate");
        }
        return this.tabDragPredicate;
    }

    /**
     * Returns the value of {@link #tabDragPredicateProperty()}.
     *
     * @return the current predicate used to determine draggable tabs,
     *         or {@code null} if all tabs are draggable
     */
    public final Predicate<Tab> getTabDragPredicate() {
        return this.tabDragPredicate == null ? null : this.tabDragPredicate.get();
    }

    /**
     * Sets the value of {@link #tabDragPredicateProperty()}.
     *
     * @param predicate the predicate to determine draggable tabs,
     *                  or {@code null} to allow dragging all tabs
     */
    public final void setTabDragPredicate(Predicate<Tab> predicate) {
        tabDragPredicateProperty().set(predicate);
    }

    /**
     * Defines whether tab dropping is enabled.
     * <p>
     * When this property is {@code true}, tabs can be dropped into this tab pane,
     * allowing users to rearrange or add tabs by dropping them.
     * When {@code false}, tab dropping is disabled and tabs cannot be dropped here.
     * <p>
     * By default, this property is {@code false}.
     *
     * @return the property representing the tab drop-enabled state
     */
    public final BooleanProperty tabDropEnabledProperty() {
        if (this.tabDropEnabled == null) {
            this.tabDropEnabled = new SimpleBooleanProperty(this, "tabDropEnabled", false);
        }
        return tabDropEnabled;
    }

    /**
     * Returns the value of {@link #tabDropEnabledProperty()}.
     *
     * @return {@code true} if tab dropping is enabled; {@code false} otherwise
     */
    public final boolean isTabDropEnabled() {
        return this.tabDropEnabled == null ? false : tabDropEnabledProperty().get();
    }

    /**
     * Sets the value of {@link #tabDropEnabledProperty()}.
     *
     * @param enabled {@code true} to enable tab dropping; {@code false} to disable it
     */
    public final void setTabDropEnabled(boolean enabled) {
        tabDropEnabledProperty().set(enabled);
    }

    /**
     * Defines the predicate that determines whether a specific tab can be dropped into this pane.
     * <p>
     * This property holds a {@link Predicate} evaluated for each {@link Tab} to decide if dropping
     * is permitted. If the predicate returns {@code true}, the tab can be dropped; if {@code false},
     * dropping is disallowed for that tab.
     * <p>
     * If the predicate is {@code null}, all tabs are allowed to be dropped by default.
     *
     * @return the property holding the drop predicate for tabs
     */
    public final ObjectProperty<Predicate<Tab>> tabDropPredicateProperty() {
        if (this.tabDropPredicate == null) {
            this.tabDropPredicate = new SimpleObjectProperty<>(this, "tabDropPredicate");
        }
        return this.tabDropPredicate;
    }

    /**
     * Returns the value of {@link #tabDropPredicateProperty()}.
     *
     * @return the current predicate used to determine droppable tabs,
     *         or {@code null} if all tabs can be dropped
     */
    public final Predicate<Tab> getTabDropPredicate() {
        return this.tabDropPredicate == null ? null : this.tabDropPredicate.get();
    }

    /**
     * Sets the value of {@link #tabDropPredicateProperty()}.
     *
     * @param predicate the predicate to determine droppable tabs,
     *                  or {@code null} to accept all tabs
     */
    public final void setTabDropPredicate(Predicate<Tab> predicate) {
        tabDropPredicateProperty().set(predicate);
    }

    /**
     * Defines the shared {@link DragAndDropContext} used to coordinate drag-and-drop operations
     * between multiple {@link TabPanePro} instances.
     * <p>
     * This property holds the context object that manages state and communication for
     * cross-pane dragging and dropping of tabs. All {@link TabPanePro} instances participating
     * in drag-and-drop must share the same context instance to enable coordinated behavior.
     * <p>
     * The property is lazily initialized when first accessed if not already set.
     * <p>
     * For drag-and-drop between multiple {@link TabPanePro} instances to work,
     * they must all share the same {@link DragAndDropContext} instance.
     *
     * @return the property holding the shared drag-and-drop context
     */
    public final ObjectProperty<DragAndDropContext> dragAndDropContextProperty() {
        if (this.dragAndDropContext == null) {
            this.dragAndDropContext = new SimpleObjectProperty<>(this, "dragAndDropContext");
        }
        return this.dragAndDropContext;
    }

    /**
     * Returns the value of {@link #dragAndDropContextProperty()}.
     *
     * @return the current shared {@link DragAndDropContext}, or {@code null} if none is set
     */
    public final DragAndDropContext getDragAndDropContext() {
        return this.dragAndDropContext == null ? null : this.dragAndDropContext.get();
    }

    /**
     * Sets the value of {@link #dragAndDropContextProperty()}.
     *
     * @param context the shared drag-and-drop context to use,
     *                or {@code null} to disable cross-pane drag-and-drop
     */
    public final void setDragAndDropContext(DragAndDropContext context) {
        dragAndDropContextProperty().set(context);
    }

    /**
     * Adds the handler that is invoked when a tab drag operation begins.
     * <p>
     * The handler is a {@link Consumer} that receives the {@link Tab} being dragged
     * at the start of the drag gesture. It can be used to perform custom logic such as
     * updating UI state, logging, or preparing drag visuals.
     */
    public final void addTabDragHandler(Consumer<Tab> handler) {
        this.tabDragHandlers.add(handler);
    }

    /**
     * Removes the handler that is invoked when a tab drag operation begins.
     * <p>
     * The handler is a {@link Consumer} that receives the {@link Tab} being dragged
     * at the start of the drag gesture. It can be used to perform custom logic such as
     * updating UI state, logging, or preparing drag visuals.
     */
    public final void removeTabDragHandler(Consumer<Tab> handler) {
        this.tabDragHandlers.remove(handler);
    }

    /**
     * Returns the handlers that are invoked when a tab drag operation begins.
     *
     * @apiNote <b>This method is for internal use only!</b>  Library clients should not call it directly, as it may
     *      change or be removed without notice in future versions.
     * @return the internal list of tab drag handlers (do not modify)
     */
    public ObservableList<Consumer<Tab>> getTabDragHandlers() {
        return tabDragHandlers;
    }

    /**
     * Adds the handler invoked when a tab drag operation ends.
     * <p>
     * The handler is a {@link BiConsumer} that receives:
     * <ul>
     *     <li>the {@link Tab} being dropped</li>
     *     <li>a {@code boolean} indicating whether the drop was successful ({@code true}) or cancelled ({@code false})</li>
     * </ul>
     * This handler can be used to perform cleanup, update UI state, or trigger side effects after a drag-and-drop completes.
     */
    public final void addTabDropHandler(BiConsumer<Tab, Boolean> handler) {
        this.tabDropHandlers.add(handler);
    }

    /**
     * Removes the handler invoked when a tab drag operation ends.
     * <p>
     * The handler is a {@link BiConsumer} that receives:
     * <ul>
     *     <li>the {@link Tab} being dropped</li>
     *     <li>a {@code boolean} indicating whether the drop was successful ({@code true}) or cancelled ({@code false})</li>
     * </ul>
     * This handler can be used to perform cleanup, update UI state, or trigger side effects after a drag-and-drop completes.
     */
    public final void removeTabDropHandler(BiConsumer<Tab, Boolean> handler) {
        this.tabDropHandlers.remove(handler);
    }

    /**
     * Returns the handlers invoked when a tab drag operation ends.
     *
     * @apiNote <b>This method is for internal use only!</b>  Library clients should not call it directly, as it may
     *      change or be removed without notice in future versions.
     * @return the internal list of tab drop handlers (do not modify)
     */
    public ObservableList<BiConsumer<Tab, Boolean>> getTabDropHandlers() {
        return tabDropHandlers;
    }
}
