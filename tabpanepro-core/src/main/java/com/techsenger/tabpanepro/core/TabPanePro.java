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
import java.util.function.Predicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author Pavel Castornii
 */
public class TabPanePro extends TabPane {

    private BooleanProperty tabDragEnabled;

    private ObjectProperty<Predicate<Tab>> tabDragFilter;

    private BooleanProperty tabDropEnabled;

    private ObjectProperty<Predicate<Tab>> tabDropFilter;

    private ObjectProperty<DragAndDropContext> dragAndDropContext;

    private ObjectProperty<EventHandler<TabEvent>> onTabDragStarted;

    private ObjectProperty<EventHandler<TabEvent>> onTabDragFinished;

    private ObjectProperty<EventHandler<TabEvent>> onTabDropped;

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
     * Defines the filter that determines whether a specific tab can be dragged from this pane.
     * <p>
     * This property holds a {@link Predicate} that is evaluated for each {@link Tab} to decide if dragging
     * is permitted. If the filter returns {@code true}, the tab can be dragged; if {@code false}, dragging
     * is disallowed for that tab.
     * <p>
     * If the filter is {@code null}, all tabs are considered draggable by default.
     * <p>
     * This filter can leverage tab-specific metadata via {@link Tab#getProperties()} to customize
     * drag behavior on a per-tab basis.
     *
     * @return the property holding the drag filter for tabs
     */
    public final ObjectProperty<Predicate<Tab>> tabDragFilterProperty() {
        if (this.tabDragFilter == null) {
            this.tabDragFilter = new SimpleObjectProperty<>(this, "tabDragFilter");
        }
        return this.tabDragFilter;
    }

    /**
     * Returns the value of {@link #tabDragFilterProperty()}.
     *
     * @return the current filter used to determine draggable tabs,
     *         or {@code null} if all tabs are draggable
     */
    public final Predicate<Tab> getTabDragFilter() {
        return this.tabDragFilter == null ? null : this.tabDragFilter.get();
    }

    /**
     * Sets the value of {@link #tabDragFilterProperty()}.
     *
     * @param filter the filter to determine draggable tabs,
     *               or {@code null} to allow dragging all tabs
     */
    public final void setTabDragFilter(Predicate<Tab> filter) {
        tabDragFilterProperty().set(filter);
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
     * Defines the filter that determines whether a specific tab can be dropped into this pane.
     * <p>
     * This property holds a {@link Predicate} evaluated for each {@link Tab} to decide if dropping
     * is permitted. If the filter returns {@code true}, the tab can be dropped; if {@code false},
     * dropping is disallowed for that tab.
     * <p>
     * If the filter is {@code null}, all tabs are allowed to be dropped by default.
     *
     * @return the property holding the drop filter for tabs
     */
    public final ObjectProperty<Predicate<Tab>> tabDropFilterProperty() {
        if (this.tabDropFilter == null) {
            this.tabDropFilter = new SimpleObjectProperty<>(this, "tabDropFilter");
        }
        return this.tabDropFilter;
    }

    /**
     * Returns the value of {@link #tabDropFilterProperty()}.
     *
     * @return the current filter used to determine droppable tabs,
     *         or {@code null} if all tabs can be dropped
     */
    public final Predicate<Tab> getTabDropFilter() {
        return this.tabDropFilter == null ? null : this.tabDropFilter.get();
    }

    /**
     * Sets the value of {@link #tabDropFilterProperty()}.
     *
     * @param filter the filter to determine droppable tabs,
     *               or {@code null} to accept all tabs
     */
    public final void setTabDropFilter(Predicate<Tab> filter) {
        tabDropFilterProperty().set(filter);
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
     * Defines the handler for {@link TabEvent#TAB_DRAG_STARTED} events.
     * <p>
     * This handler is invoked when a tab drag operation starts on this pane.
     *
     * @return the property holding the TAB_DRAG_STARTED event handler
     */
    public final ObjectProperty<EventHandler<TabEvent>> onTabDragStartedProperty() {
        if (this.onTabDragStarted == null) {
            this.onTabDragStarted = new ObjectPropertyBase<>() {
                @Override
                protected void invalidated() {
                    setEventHandler(TabEvent.TAB_DRAG_STARTED, get());
                }

                @Override
                public Object getBean() {
                    return TabPanePro.this;
                }

                @Override
                public String getName() {
                    return "onTabDragStarted";
                }
            };
        }
        return onTabDragStarted;
    }

    /**
     * Returns the value of {@link #onTabDragStartedProperty()}.
     *
     * @return the current TAB_DRAG_STARTED event handler, or {@code null} if none is set
     */
    public final EventHandler<TabEvent> getOnTabDragStarted() {
        return onTabDragStartedProperty().get();
    }

    /**
     * Sets the value of {@link #onTabDragStartedProperty()}.
     *
     * @param handler the handler to invoke when a tab drag starts,
     *                or {@code null} to remove the current handler
     */
    public final void setOnTabDragStarted(EventHandler<TabEvent> handler) {
        onTabDragStartedProperty().set(handler);
    }

    /**
     * Defines the handler for {@link TabEvent#TAB_DRAG_FINISHED} events.
     * <p>
     * This handler is invoked when a tab drag operation finishes on this pane, whether
     * the drop was successful or cancelled. Use {@link TabEvent#isSuccess()} to determine
     * the outcome. The target {@link TabPanePro} is available via
     * {@link DragAndDropContext#getTargetTabPane()}.
     *
     * @return the property holding the TAB_DRAG_FINISHED event handler
     */
    public final ObjectProperty<EventHandler<TabEvent>> onTabDragFinishedProperty() {
        if (this.onTabDragFinished == null) {
            this.onTabDragFinished = new ObjectPropertyBase<>() {
                @Override
                protected void invalidated() {
                    setEventHandler(TabEvent.TAB_DRAG_FINISHED, get());
                }

                @Override
                public Object getBean() {
                    return TabPanePro.this;
                }

                @Override
                public String getName() {
                    return "onTabDragFinished";
                }
            };
        }
        return onTabDragFinished;
    }

    /**
     * Returns the value of {@link #onTabDragFinishedProperty()}.
     *
     * @return the current TAB_DRAG_FINISHED event handler, or {@code null} if none is set
     */
    public final EventHandler<TabEvent> getOnTabDragFinished() {
        return onTabDragFinishedProperty().get();
    }

    /**
     * Sets the value of {@link #onTabDragFinishedProperty()}.
     *
     * @param handler the handler to invoke when a tab drag finishes,
     *                or {@code null} to remove the current handler
     */
    public final void setOnTabDragFinished(EventHandler<TabEvent> handler) {
        onTabDragFinishedProperty().set(handler);
    }

    /**
     * Defines the handler for {@link TabEvent#TAB_DROPPED} events.
     * <p>
     * This handler is invoked when a tab is successfully dropped into this pane.
     *
     * @return the property holding the TAB_DROPPED event handler
     */
    public final ObjectProperty<EventHandler<TabEvent>> onTabDroppedProperty() {
        if (this.onTabDropped == null) {
            this.onTabDropped = new ObjectPropertyBase<>() {
                @Override
                protected void invalidated() {
                    setEventHandler(TabEvent.TAB_DROPPED, get());
                }

                @Override
                public Object getBean() {
                    return TabPanePro.this;
                }

                @Override
                public String getName() {
                    return "onTabDropped";
                }
            };
        }
        return onTabDropped;
    }

    /**
     * Returns the value of {@link #onTabDroppedProperty()}.
     *
     * @return the current TAB_DROPPED event handler, or {@code null} if none is set
     */
    public final EventHandler<TabEvent> getOnTabDropped() {
        return onTabDroppedProperty().get();
    }

    /**
     * Sets the value of {@link #onTabDroppedProperty()}.
     *
     * @param handler the handler to invoke when a tab is dropped into this pane,
     *                or {@code null} to remove the current handler
     */
    public final void setOnTabDropped(EventHandler<TabEvent> handler) {
        onTabDroppedProperty().set(handler);
    }
}
