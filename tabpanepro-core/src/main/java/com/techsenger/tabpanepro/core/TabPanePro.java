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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private ObjectProperty<Consumer<Tab>> tabDragHandler;

    private ObjectProperty<BiConsumer<Tab, Boolean>> tabDropHandler;

    public TabPanePro() {
        this(null);
    }

    public TabPanePro(Tab... tabs) {
        super(tabs);
        setSkin(new TabPaneProSkin(this));
        getStyleClass().add("tab-pane-pro");
    }


    /**
     * The property representing the tab drag-enabled state.
     * @return the BooleanProperty instance
     */
    public BooleanProperty tabDragEnabledProperty() {
        if (this.tabDragEnabled == null) {
            this.tabDragEnabled = new SimpleBooleanProperty(this, "tabDragEnabled", false);
        }
        return tabDragEnabled;
    }

    /**
     * Gets the current value of the tab drag-enabled property.
     * @return true if tab dragging is enabled, false otherwise
     */
    public boolean isTabDragEnabled() {
        return this.tabDragEnabled == null ? false : tabDragEnabledProperty().get();
    }

    /**
     * Sets whether tab dragging is enabled.
     * @param enabled true to enable tab dragging, false to disable
     */
    public void setTabDragEnabled(boolean enabled) {
        tabDragEnabledProperty().set(enabled);
    }

    /**
     * The drag predicate property that determines whether a tab can be dragged from this pane.
     * <p>
     * The predicate evaluates to {@code true} if the tab can be dragged. When is {@code null} all tabs are draggable.
     * Can be combined with {@link Tab#getProperties()} for tab-specific control.
     *
     * @return the drag predicate property
     */
    public ObjectProperty<Predicate<Tab>> tabDragPredicateProperty() {
        if (this.tabDragPredicate == null) {
            this.tabDragPredicate = new SimpleObjectProperty<>(this, "tabDragPredicate");
        }
        return this.tabDragPredicate;
    }

    /**
     * Gets the value of the drag predicate property.
     *
     * @return the current drag predicate, or {@code null} if all tabs are draggable
     */
    public Predicate<Tab> getTabDragPredicate() {
        return this.tabDragPredicate == null ? null : this.tabDragPredicate.get();
    }

    /**
     * Sets the drag predicate property.
     *
     * @param predicate the new predicate to use, or {@code null} to allow dragging all tabs
     */
    public void setTabDragPredicate(Predicate<Tab> predicate) {
        tabDragPredicateProperty().set(predicate);
    }

    /**
     * The property representing the tab drop-enabled state.
     * @return the BooleanProperty instance
     */
    public BooleanProperty tabDropEnabledProperty() {
        if (this.tabDropEnabled == null) {
            this.tabDropEnabled = new SimpleBooleanProperty(this, "tabDropEnabled", false);
        }
        return tabDropEnabled;
    }

    /**
     * Gets the current value of the tab drop-enabled property.
     * @return true if tab dropping is enabled, false otherwise
     */
    public boolean isTabDropEnabled() {
        return this.tabDropEnabled == null ? false : tabDropEnabledProperty().get();
    }

    /**
     * Sets whether tab dropping is enabled.
     * @param enabled true to enable tab dropping, false to disable
     */
    public void setTabDropEnabled(boolean enabled) {
        tabDropEnabledProperty().set(enabled);
    }

    /**
     * The drop predicate property that determines whether a tab can be dropped into this pane.
     * <p>
     * The predicate evaluates to {@code true} if the tab can be dropped. When is {@code null}, all tabs
     * can be dropped into this pane.
     *
     * @return the drop predicate property
     */
    public ObjectProperty<Predicate<Tab>> tabDropPredicateProperty() {
        if (this.tabDropPredicate == null) {
            this.tabDropPredicate = new SimpleObjectProperty<>(this, "tabDropPredicate");
        }
        return this.tabDropPredicate;
    }

    /**
     * Gets the value of the drop predicate property.
     *
     * @return the current drop predicate, or {@code null} if all tabs can be dropped
     */
    public Predicate<Tab> getTabDropPredicate() {
        return this.tabDropPredicate == null ? null : this.tabDropPredicate.get();
    }

    /**
     * Sets the drop predicate property.
     *
     * @param predicate the new predicate to use, or {@code null} to accept all tabs
     */
    public void setTabDropPredicate(Predicate<Tab> predicate) {
        tabDropPredicateProperty().set(predicate);
    }

    /**
     * Returns the {@link ObjectProperty} holding the shared {@link DragAndDropContext}
     * used for drag-and-drop operations between {@link TabPanePro} instances.
     * <p>
     * All {@link TabPanePro}s that participate in cross-pane drag-and-drop must share
     * the same context. If uninitialized, this property is lazily created.
     * </p>
     *
     * @return The {@link ObjectProperty} containing the drag-and-drop context.
     */
    public ObjectProperty<DragAndDropContext> dragAndDropContextProperty() {
        if (this.dragAndDropContext == null) {
            this.dragAndDropContext = new SimpleObjectProperty<>(this, "dragAndDropContext");
        }
        return this.dragAndDropContext;
    }

    /**
     * Gets the current shared {@link DragAndDropContext} used for cross-{@link TabPanePro}
     * drag-and-drop operations.
     *
     * @return The current context, or {@code null} if none is set.
     * @see #dragAndDropContextProperty()
     */
    public DragAndDropContext getDragAndDropContext() {
        return this.dragAndDropContext == null ? null : this.dragAndDropContext.get();
    }

    /**
     * Sets the shared {@link DragAndDropContext} to enable drag-and-drop
     * between multiple {@link TabPanePro} instances.
     * <p>
     * <b>Note:</b> All participating {@link TabPanePro}s must use the same context.
     * </p>
     *
     * @param context The {@link DragAndDropContext} to share, or {@code null} to disable.
     * @see #dragAndDropContextProperty()
     */
    public void setDragAndDropContext(DragAndDropContext context) {
        dragAndDropContextProperty().set(context);
    }

    /**
     * The handler that is called when a tab drag operation starts.
     * <p>
     * The provided {@code Consumer<Tab>} will receive the tab being dragged.
     *
     * @return the tab drag handler property
     */
    public final ObjectProperty<Consumer<Tab>> tabDragHandlerProperty() {
        if (tabDragHandler == null) {
            tabDragHandler = new SimpleObjectProperty<>(this, "tabDragHandler");
        }
        return tabDragHandler;
    }

    /**
     * Sets the handler to be called when a tab drag operation starts.
     *
     * @param handler the tab drag handler
     */
    public final void setTabDragHandler(Consumer<Tab> handler) {
        tabDragHandlerProperty().set(handler);
    }

    /**
     * Returns the handler that is called when a tab drag operation starts.
     *
     * @return the tab drag handler
     */
    public final Consumer<Tab> getTabDragHandler() {
        return tabDragHandler == null ? null : tabDragHandler.get();
    }

    /**
     * The handler that is called when a tab drag operation ends.
     * <p>
     * The provided {@code BiConsumer<Tab, Boolean>} will receive:
     * <ul>
     *     <li>the tab being dropped</li>
     *     <li>{@code true} if the drop was successful, or {@code false} if it was cancelled</li>
     * </ul>
     *
     * @return the tab drop handler property
     */
    public final ObjectProperty<BiConsumer<Tab, Boolean>> tabDropHandlerProperty() {
        if (tabDropHandler == null) {
            tabDropHandler = new SimpleObjectProperty<>(this, "tabDropHandler");
        }
        return tabDropHandler;
    }

    /**
     * Sets the handler to be called when a tab drag operation ends.
     *
     * @param handler the tab drop handler
     */
    public final void setTabDropHandler(BiConsumer<Tab, Boolean> handler) {
        tabDropHandlerProperty().set(handler);
    }

    /**
     * Returns the handler that is called when a tab drag operation ends.
     *
     * @return the tab drop handler
     */
    public final BiConsumer<Tab, Boolean> getTabDropHandler() {
        return tabDropHandler == null ? null : tabDropHandler.get();
    }
}
