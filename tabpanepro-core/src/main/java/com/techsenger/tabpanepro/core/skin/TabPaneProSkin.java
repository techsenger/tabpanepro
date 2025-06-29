/*
 * Copyright (c) 2011, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This source file was taken from the OpenJFX project (https://github.com/openjdk/jfx),
 * commit 72c1c21a76ba752439c877aba599b0b5f8bf9332 (tag: 25+20), and modified on:
 * June 18, 2025; June 20, 2025; June 21, 2025; June 22, 2025; June 23, 2025; June 24, 2025;
 * June 25, 2025; June 26, 2025.
 */

package com.techsenger.tabpanepro.core.skin;

import com.techsenger.tabpanepro.core.TabPanePro;
import com.techsenger.tabpanepro.core.behavior.TabPaneBehavior;
import com.techsenger.tabpanepro.core.control.LambdaMultiplePropertyChangeListenerHandler;
import com.techsenger.tabpanepro.core.control.TabObservableList;
import com.techsenger.tabpanepro.core.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.EnumConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import static javafx.geometry.Side.BOTTOM;
import static javafx.geometry.Side.LEFT;
import static javafx.geometry.Side.RIGHT;
import static javafx.geometry.Side.TOP;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TabPane.TabDragPolicy;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Default skin implementation for the {@link TabPanePro} control.
 *
 * @see TabPanePro
 */
public class TabPaneProSkin extends SkinBase<TabPanePro> {

    /* *************************************************************************
     *                                                                         *
     * Enums                                                                   *
     *                                                                         *
     **************************************************************************/

    private enum TabAnimation {
        NONE,
        GROW
        // In future we could add FADE, ...
    }

    private enum TabAnimationState {
        SHOWING, HIDING, NONE;
    }



    /* *************************************************************************
     *                                                                         *
     * Static fields                                                           *
     *                                                                         *
     **************************************************************************/

    static int CLOSE_BTN_SIZE = 16;



    /* *************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private static final double ANIMATION_SPEED = 150;

    private TabHeaderArea tabHeaderArea;
    private ObservableList<TabContentRegion> tabContentRegions;
    private Rectangle clipRect;
    private Rectangle tabHeaderAreaClipRect;
    private Tab selectedTab;

    private final TabPaneBehavior behavior;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(TabPaneProSkin.class
            .getPackage().getName() + ".controls");

    /* *************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new TabPaneProSkin instance, installing the necessary child
     * nodes into the Control {@link Control#getChildren() children} list, as
     * well as the necessary input mappings for handling key, mouse, etc events.
     *
     * @param control The control that this skin should be installed onto.
     */
    public TabPaneProSkin(TabPanePro control) {
        super(control);

        // install default input map for the TabPane control
        this.behavior = new TabPaneBehavior(control);
//        control.setInputMap(behavior.getInputMap());

        clipRect = new Rectangle(control.getWidth(), control.getHeight());
        getSkinnable().setClip(clipRect);

        tabContentRegions = FXCollections.<TabContentRegion>observableArrayList();

        for (Tab tab : getSkinnable().getTabs()) {
            addTabContent(tab);
        }

        tabHeaderAreaClipRect = new Rectangle();
        tabHeaderArea = new TabHeaderArea();
        tabHeaderArea.setClip(tabHeaderAreaClipRect);
        getChildren().add(tabHeaderArea);

        initializeTabListener();
        updateSelectionModel();

        registerChangeListener(control.selectionModelProperty(), e -> updateSelectionModel());
        registerChangeListener(control.sideProperty(), e -> {
            updateTabPosition();
            tabHeaderArea.updateElementsRotation();
        });
        registerChangeListener(control.widthProperty(), e -> {
            tabHeaderArea.invalidateScrollOffset();
            clipRect.setWidth(getSkinnable().getWidth());
        });
        registerChangeListener(control.heightProperty(), e -> {
            tabHeaderArea.invalidateScrollOffset();
            clipRect.setHeight(getSkinnable().getHeight());
        });

        selectedTab = getSkinnable().getSelectionModel().getSelectedItem();
        // Could not find the selected tab try and get the selected tab using the selected index
        if (selectedTab == null && getSkinnable().getSelectionModel().getSelectedIndex() != -1) {
            getSkinnable().getSelectionModel().select(getSkinnable().getSelectionModel().getSelectedIndex());
            selectedTab = getSkinnable().getSelectionModel().getSelectedItem();
        }
        if (selectedTab == null) {
            // getSelectedItem and getSelectedIndex failed select the first.
            getSkinnable().getSelectionModel().selectFirst();
        }
        selectedTab = getSkinnable().getSelectionModel().getSelectedItem();

        initializeSwipeHandlers();
    }

    /* *************************************************************************
     *                                                                         *
     * TabPanePro                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Returns the first area, located before all tab headers in the tab header area.
     * <p>
     * This area is typically used to place custom UI elements, such as a menu button.
     *
     * @return the {@code StackPane} positioned before the tab headers
     */
    public StackPane getTabHeaderFirstArea() {
        return tabHeaderArea.headerFirstArea;
    }

    /**
     * Returns the last area, located after the sticky area and all tab headers in the tab header area.
     * <p>
     * This area is typically used to place scroll buttons, a minimize button, or other auxiliary controls.
     *
     * @return the {@code StackPane} positioned after the tab headers and sticky area
     */
    public StackPane getTabHeaderLastArea() {
        return tabHeaderArea.headerLastArea;
    }

    /**
     * Returns the sticky area, located between the tab headers and the last header area.
     * <p>
     * This area is typically used to place a "New Tab" button or other controls that should remain visible
     * regardless of tab scrolling.
     *
     * @return the {@code StackPane} positioned between the tab headers and the last header area
     */
    public StackPane getTabHeaderStickyArea() {
        return tabHeaderArea.headerStickyArea;
    }

    /**
     * Returns the property that controls the visibility policy of the tab header area.
     * <p>
     * By default, the tab header area becomes invisible when there are no tabs. This behavior can be changed using
     * the {@link TabHeaderAreaPolicy} enum.
     *
     * @return the {@code ObjectProperty} holding the current {@code TabHeaderAreaPolicy}
     */
    public ObjectProperty<TabHeaderAreaPolicy> tabHeaderAreaPolicyProperty() {
        return tabHeaderArea.policy;
    }

    /**
     * Returns the current {@link TabHeaderAreaPolicy} that determines the visibility of the tab header area.
     *
     * @return the current tab header area policy
     */
    public TabHeaderAreaPolicy getTabHeaderAreaPolicy() {
        return tabHeaderArea.policy.get();
    }

    /**
     * Sets the {@link TabHeaderAreaPolicy} that controls the visibility of the tab header area.
     *
     * @param policy the new tab header area policy to apply
     */
    public void setTabHeaderAreaPolicy(TabHeaderAreaPolicy policy) {
        this.tabHeaderArea.policy.set(policy);
    }

    /**
     * Returns the property that controls whether a scroll bar is enabled
     * next to the tab headers when the tabs overflow.
     *
     * @return the tabScrollBarEnabled property
     */
    public BooleanProperty tabScrollBarEnabledProperty() {
        return this.tabHeaderArea.scrollBarEnabled;
    }

    /**
     * Returns whether a scroll bar is enabled next to the tab headers
     * when the tabs overflow.
     *
     * @return {@code true} if the scroll bar is enabled, {@code false} otherwise
     */
    public boolean isTabScrollBarEnabled() {
        return this.tabHeaderArea.scrollBarEnabled.get();
    }

    /**
     * Sets whether a scroll bar is enabled next to the tab headers
     * when the tabs overflow.
     *
     * @param enabled {@code true} to enable the scroll bar, {@code false} to disable it
     */
    public void setTabScrollBarEnabled(boolean enabled) {
        this.tabHeaderArea.scrollBarEnabled.set(enabled);
    }

    /**
     * Shows the standard tabs menu at the specified anchor node.
     * <p>
     * This menu is typically used for quick navigation between tabs and is usually triggered by a button placed in
     * the tab header area.
     *
     * @param anchor the node relative to which the popup menu will be shown, typically a button that opens the menu
     */
    public void showTabsMenu(Node anchor) {
        this.tabHeaderArea.tabsMenuManager.showPopupMenu(anchor);
    }

    /**
     * Returns a read-only property indicating whether all tab headers fit within the headers region.
     * <p>
     * This property can be used to determine whether a tabs menu button should be shown, for example, when the
     * available space is insufficient to display all tabs.
     *
     * @return the read-only boolean property indicating tab header overflow
     */
    public ReadOnlyBooleanProperty headersRegionOverflowedProperty() {
        return this.tabHeaderArea.headersRegionOverflowed.getReadOnlyProperty();
    }

    /**
     * Returns whether the headers region is currently overflowed, meaning not all tab headers fit within the
     * visible area.
     * <p>
     * This is typically used to decide whether a tabs menu button should be displayed.
     *
     * @return {@code true} if not all tab headers are visible; {@code false} otherwise
     */
    public boolean isHeadersRegionOverflowed() {
        return this.tabHeaderArea.headersRegionOverflowed.get();
    }

    /**
     * Returns a read-only property representing the width of the headers region.
     *
     * @return the read-only double property of the headers region width
     */
    public ReadOnlyDoubleProperty headersRegionWidthProperty() {
        return this.tabHeaderArea.headersRegion.widthProperty();
    }

    /**
     * Returns the current width of the headers region.
     *
     * @return the width of the headers region in pixels
     */
    public double getHeadersRegionWidth() {
        return this.tabHeaderArea.headersRegion.widthProperty().get();
    }

    /**
     * Returns a read-only property representing the offset of the headers region.
     * <p>
     * This offset indicates how much the entire headers region is shifted (scrolled), which effectively controls
     * the visible portion of the tab headers.
     *
     * @return the read-only double property of the headers region offset
     */
    public ReadOnlyDoubleProperty headersRegionOffsetProperty() {
        return this.tabHeaderArea.scrollOffset;
    }

    /**
     * Returns the current offset of the headers region.
     * <p>
     * This value reflects how much the headers region has been scrolled (shifted), which effectively controls
     * the visible portion of the tab headers.
     *
     * @return the horizontal offset in pixels of the headers region
     */
    public double getHeadersRegionOffset() {
        return this.tabHeaderArea.scrollOffset.get();
    }

    /**
     * Returns a read-only property representing the width of the clip region for the tab headers.
     * <p>
     * This clip region acts as a viewport, defining the visible area through which the tab headers are displayed.
     *
     * @return the read-only double property of the headers clip width
     */
    public ReadOnlyDoubleProperty headersClipWidthProperty() {
        return this.tabHeaderArea.headerClip.widthProperty();
    }

    /**
     * Returns the current width of the clip region for the tab headers.
     * <p>
     * The clip defines the viewport area that determines which part of the tab headers is visible.
     *
     * @return the width of the headers clip region in pixels
     */
    public double getHeadersClipWidth() {
        return this.tabHeaderArea.headerClip.widthProperty().get();
    }

    /**
     * Scrolls the tab headers by the specified number of pixels.
     *
     * @param delta the amount in pixels to scroll the tab headers;
     */
    public void scrollTabHeadersBy(double delta) {
        this.tabHeaderArea.scrollTabsBy(delta);
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    private ObjectProperty<TabAnimation> openTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW) {
        @Override public CssMetaData<TabPane,TabAnimation> getCssMetaData() {
            return StyleableProperties.OPEN_TAB_ANIMATION;
        }

        @Override public Object getBean() {
            return TabPaneProSkin.this;
        }

        @Override public String getName() {
            return "openTabAnimation";
        }
    };

    private ObjectProperty<TabAnimation> closeTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW) {
        @Override public CssMetaData<TabPane,TabAnimation> getCssMetaData() {
            return StyleableProperties.CLOSE_TAB_ANIMATION;
        }

        @Override public Object getBean() {
            return TabPaneProSkin.this;
        }

        @Override public String getName() {
            return "closeTabAnimation";
        }
    };

    /* *************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override public void dispose() {
        if (getSkinnable() == null) return;

        if (selectionModel != null) {
            selectionModel.selectedItemProperty().removeListener(weakSelectionChangeListener);
            selectionModel = null;
        }
        getSkinnable().getTabs().removeListener(weakTabsListener);
        tabHeaderArea.dispose();

        // Control and Skin share the list of children, so children that are
        // added by Skin are actually added to control's list of children,
        // so a skin should remove the children that it adds.
        getChildren().remove(tabHeaderArea);
        for (Tab tab : getSkinnable().getTabs()) {
            removeTabContent(tab);
        }

        super.dispose();

        if (behavior != null) {
            behavior.dispose();
        }
    }

    /** {@inheritDoc} */
    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        // The TabPane can only be as wide as it widest content width.
        double maxw = 0.0;
        for (TabContentRegion contentRegion: tabContentRegions) {
            maxw = Math.max(maxw, snapSizeX(contentRegion.prefWidth(-1)));
        }

        final boolean isHorizontal = isHorizontal();
        final double tabHeaderAreaSize = isHorizontal
                ? snapSizeX(tabHeaderArea.prefWidth(-1))
                : snapSizeY(tabHeaderArea.prefHeight(-1));

        double prefWidth = isHorizontal ?
                Math.max(maxw, tabHeaderAreaSize) : maxw + tabHeaderAreaSize;
        return snapSizeX(prefWidth) + rightInset + leftInset;
    }

    /** {@inheritDoc} */
    @Override protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        // The TabPane can only be as high as it highest content height.
        double maxh = 0.0;
        for (TabContentRegion contentRegion: tabContentRegions) {
            maxh = Math.max(maxh, snapSizeY(contentRegion.prefHeight(-1)));
        }

        final boolean isHorizontal = isHorizontal();
        final double tabHeaderAreaSize = isHorizontal
                ? snapSizeY(tabHeaderArea.prefHeight(-1))
                : snapSizeX(tabHeaderArea.prefWidth(-1));

        double prefHeight = isHorizontal ?
                maxh + snapSizeY(tabHeaderAreaSize) : Math.max(maxh, tabHeaderAreaSize);
        return snapSizeY(prefHeight) + topInset + bottomInset;
    }

    /** {@inheritDoc} */
    @Override public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        Side tabPosition = getSkinnable().getSide();
        if (tabPosition == Side.TOP) {
            return tabHeaderArea.getBaselineOffset() + topInset;
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override protected void layoutChildren(final double x, final double y,
                                            final double w, final double h) {
        TabPane tabPane = getSkinnable();
        Side tabPosition = tabPane.getSide();

        double headerHeight = tabPosition.isHorizontal()
                ? snapSizeY(tabHeaderArea.prefHeight(-1))
                : snapSizeX(tabHeaderArea.prefHeight(-1));
        double tabsStartX = tabPosition.equals(Side.RIGHT)? x + w - headerHeight : x;
        double tabsStartY = tabPosition.equals(Side.BOTTOM)? y + h - headerHeight : y;

        final double leftInset = snappedLeftInset();
        final double topInset = snappedTopInset();

        if (tabPosition == Side.TOP) {
            tabHeaderArea.resize(w, headerHeight);
            tabHeaderArea.relocate(tabsStartX, tabsStartY);
            tabHeaderArea.getTransforms().clear();
            tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.TOP)));
        } else if (tabPosition == Side.BOTTOM) {
            tabHeaderArea.resize(w, headerHeight);
            tabHeaderArea.relocate(w + leftInset, tabsStartY - headerHeight);
            tabHeaderArea.getTransforms().clear();
            tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.BOTTOM), 0, headerHeight));
        } else if (tabPosition == Side.LEFT) {
            tabHeaderArea.resize(h, headerHeight);
            tabHeaderArea.relocate(tabsStartX + headerHeight, h - headerHeight + topInset);
            tabHeaderArea.getTransforms().clear();
            tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.LEFT), 0, headerHeight));
        } else if (tabPosition == Side.RIGHT) {
            tabHeaderArea.resize(h, headerHeight);
            tabHeaderArea.relocate(tabsStartX, y - headerHeight);
            tabHeaderArea.getTransforms().clear();
            tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.RIGHT), 0, headerHeight));
        }

        tabHeaderAreaClipRect.setX(0);
        tabHeaderAreaClipRect.setY(0);
        if (isHorizontal()) {
            tabHeaderAreaClipRect.setWidth(w);
        } else {
            tabHeaderAreaClipRect.setWidth(h);
        }
        tabHeaderAreaClipRect.setHeight(headerHeight);

        // ==================================
        // position the tab content for the selected tab only
        // ==================================
        // if the tabs are on the left, the content needs to be indented
        double contentStartX = 0;
        double contentStartY = 0;

        if (tabPosition == Side.TOP) {
            contentStartX = x;
            contentStartY = y + headerHeight;
            if (isFloatingStyleClass()) {
                // This is to hide the top border content
                contentStartY -= 1;
            }
        } else if (tabPosition == Side.BOTTOM) {
            contentStartX = x;
            contentStartY = y + topInset;
            if (isFloatingStyleClass()) {
                // This is to hide the bottom border content
                contentStartY = 1 + topInset;
            }
        } else if (tabPosition == Side.LEFT) {
            contentStartX = x + headerHeight;
            contentStartY = y;
            if (isFloatingStyleClass()) {
                // This is to hide the left border content
                contentStartX -= 1;
            }
        } else if (tabPosition == Side.RIGHT) {
            contentStartX = x + leftInset;
            contentStartY = y;
            if (isFloatingStyleClass()) {
                // This is to hide the right border content
                contentStartX = 1 + leftInset;
            }
        }

        double contentWidth = w - (isHorizontal() ? 0 : headerHeight);
        double contentHeight = h - (isHorizontal() ? headerHeight: 0);

        for (int i = 0, max = tabContentRegions.size(); i < max; i++) {
            TabContentRegion tabContent = tabContentRegions.get(i);

            tabContent.setAlignment(Pos.TOP_LEFT);
            if (tabContent.getClip() != null) {
                ((Rectangle)tabContent.getClip()).setWidth(contentWidth);
                ((Rectangle)tabContent.getClip()).setHeight(contentHeight);
            }

            // we need to size all tabs, even if they aren't visible. For example,
            // see JDK-8116643
            tabContent.resize(contentWidth, contentHeight);
            tabContent.relocate(contentStartX, contentStartY);
        }
    }

    /* *************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    private SelectionModel<Tab> selectionModel;
    private InvalidationListener selectionChangeListener = observable -> {
        tabHeaderArea.invalidateScrollOffset();
        selectedTab = getSkinnable().getSelectionModel().getSelectedItem();
        getSkinnable().requestLayout();
    };
    private WeakInvalidationListener weakSelectionChangeListener =
            new WeakInvalidationListener(selectionChangeListener);

    private void updateSelectionModel() {
        if (selectionModel != null) {
            selectionModel.selectedItemProperty().removeListener(weakSelectionChangeListener);
        }
        selectionModel = getSkinnable().getSelectionModel();
        if (selectionModel != null) {
            selectionModel.selectedItemProperty().addListener(weakSelectionChangeListener);
        }
    }

    private static int getRotation(Side pos) {
        switch (pos) {
            case TOP:
                return 0;
            case BOTTOM:
                return 180;
            case LEFT:
                return -90;
            case RIGHT:
                return 90;
            default:
                return 0;
        }
    }

    /**
     * VERY HACKY - this lets us 'duplicate' Label and ImageView nodes to be used in a
     * Tab and the tabs menu at the same time.
     */
    private static Node clone(Node n) {
        if (n == null) {
            return null;
        }
        if (n instanceof ImageView) {
            ImageView iv = (ImageView) n;
            ImageView imageview = new ImageView();
            imageview.imageProperty().bind(iv.imageProperty());
            return imageview;
        }
        if (n instanceof Label) {
            Label l = (Label)n;
            Label label = new Label(l.getText(), clone(l.getGraphic()));
            label.textProperty().bind(l.textProperty());
            return label;
        }
        return null;
    }

    private void removeTabs(List<? extends Tab> removedList) {
        for (final Tab tab : removedList) {
            stopCurrentAnimation(tab);
            // Animate the tab removal
            final TabHeaderSkin tabRegion = tabHeaderArea.getTabHeaderSkin(tab);
            if (tabRegion != null) {
                tabRegion.isClosing = true;

                tabRegion.dispose();
                removeTabContent(tab);

                EventHandler<ActionEvent> cleanup = ae -> {
                    tabRegion.animationState = TabAnimationState.NONE;

                    tabHeaderArea.removeTab(tab);
                    tabHeaderArea.requestLayout();
                };

                if (Platform.isFxApplicationThread() && (closeTabAnimation.get() == TabAnimation.GROW)) {
                    tabRegion.animationState = TabAnimationState.HIDING;
                    Timeline closedTabTimeline = tabRegion.currentAnimation =
                            createTimeline(tabRegion, Duration.millis(ANIMATION_SPEED), 0.0F, cleanup);
                    closedTabTimeline.play();
                } else {
                    cleanup.handle(null);
                }
            }
        }
    }

    private void stopCurrentAnimation(Tab tab) {
        final TabHeaderSkin tabRegion = tabHeaderArea.getTabHeaderSkin(tab);
        if (tabRegion != null) {
            // Execute the code immediately, don't wait for the animation to finish.
            Timeline timeline = tabRegion.currentAnimation;
            if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.getOnFinished().handle(null);
                timeline.stop();
                tabRegion.currentAnimation = null;
            }
        }
    }

    private void addTabs(List<? extends Tab> addedList, int from) {
        int i = 0;

        // JDK-8093620: check if any other tabs are animating - they must be completed first.
        List<Node> headers = new ArrayList<>(tabHeaderArea.headersRegion.getChildren());
        for (Node n : headers) {
            TabHeaderSkin header = (TabHeaderSkin) n;
            if (header.animationState == TabAnimationState.HIDING) {
                stopCurrentAnimation(header.tab);
            }
        }
        // end of fix for JDK-8093620

        for (final Tab tab : addedList) {
            stopCurrentAnimation(tab); // Note that this must happen before addTab() call below
            // A new tab was added - animate it out
            int index = from + i++;
            tabHeaderArea.addTab(tab, index);
            addTabContent(tab);
            final TabHeaderSkin tabRegion = tabHeaderArea.getTabHeaderSkin(tab);
            if (tabRegion != null) {
                if (Platform.isFxApplicationThread() && (openTabAnimation.get() == TabAnimation.GROW)) {
                    tabRegion.animationState = TabAnimationState.SHOWING;
                    tabRegion.animationTransition.setValue(0.0);
                    tabRegion.setVisible(true);
                    tabRegion.currentAnimation = createTimeline(tabRegion, Duration.millis(ANIMATION_SPEED), 1.0, event -> {
                        tabRegion.animationState = TabAnimationState.NONE;
                        tabRegion.setVisible(true);
                        tabRegion.inner.requestLayout();
                    });
                    tabRegion.currentAnimation.play();
                } else {
                    tabRegion.setVisible(true);
                    tabRegion.inner.requestLayout();
                }
            }
        }
    }

    ListChangeListener<Tab> tabsListener;
    WeakListChangeListener<Tab> weakTabsListener;
    private void initializeTabListener() {
        tabsListener = c -> {
            tabHeaderArea.updateNoTabsState(); // before processing tab adding/removing!

            List<Tab> tabsToRemove = new ArrayList<>();
            List<Tab> tabsToAdd = new ArrayList<>();
            while (c.next()) {
                if (c.wasPermutated()) {
                    if (dragState != DragState.REORDER) {
                        TabPane tabPane = getSkinnable();
                        List<Tab> tabs = tabPane.getTabs();

                        // tabs sorted : create list of permutated tabs.
                        // clear selection, set tab animation to NONE
                        // remove permutated tabs, add them back in correct order.
                        // restore old selection, and old tab animation states.
                        int size = c.getTo() - c.getFrom();
                        Tab selTab = tabPane.getSelectionModel().getSelectedItem();
                        List<Tab> permutatedTabs = new ArrayList<>(size);
                        getSkinnable().getSelectionModel().clearSelection();

                        // save and set tab animation to none - as it is not a good idea
                        // to animate on the same data for open and close.
                        TabAnimation prevOpenAnimation = openTabAnimation.get();
                        TabAnimation prevCloseAnimation = closeTabAnimation.get();
                        openTabAnimation.set(TabAnimation.NONE);
                        closeTabAnimation.set(TabAnimation.NONE);
                        for (int i = c.getFrom(); i < c.getTo(); i++) {
                            permutatedTabs.add(tabs.get(i));
                        }

                        removeTabs(permutatedTabs);
                        addTabs(permutatedTabs, c.getFrom());
                        openTabAnimation.set(prevOpenAnimation);
                        closeTabAnimation.set(prevCloseAnimation);
                        getSkinnable().getSelectionModel().select(selTab);
                    }
                }

                if (c.wasRemoved()) {
                    tabsToRemove.addAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    tabsToAdd.addAll(c.getAddedSubList());
                }
            }

            // now only remove the tabs that are not in the tabsToAdd list
            tabsToRemove.removeAll(tabsToAdd);
            removeTabs(tabsToRemove);

            // and add in any new tabs (that we don't already have showing)
            List<Pair<Integer, TabHeaderSkin>> headersToMove = new ArrayList();
            if (!tabsToAdd.isEmpty()) {
                for (TabContentRegion tabContentRegion : tabContentRegions) {
                    Tab tab = tabContentRegion.getTab();
                    TabHeaderSkin tabHeader = tabHeaderArea.getTabHeaderSkin(tab);
                    if (!tabHeader.isClosing && tabsToAdd.contains(tabContentRegion.getTab())) {
                        tabsToAdd.remove(tabContentRegion.getTab());

                        // If a tab is removed and added back at the same time,
                        // then we must ensure that the index of tabHeader in
                        // headersRegion is same as index of tab in getTabs().
                        int tabIndex = getSkinnable().getTabs().indexOf(tab);
                        int tabHeaderIndex = tabHeaderArea.headersRegion.getChildren().indexOf(tabHeader);
                        if (tabIndex != tabHeaderIndex) {
                            headersToMove.add(new Pair(tabIndex, tabHeader));
                        }
                    }
                }

                if (!tabsToAdd.isEmpty()) {
                    addTabs(tabsToAdd, getSkinnable().getTabs().indexOf(tabsToAdd.get(0)));
                }
                for (Pair<Integer, TabHeaderSkin> move : headersToMove) {
                    tabHeaderArea.moveTab(move.getKey(), move.getValue());
                }
            }

            // Fix for JDK-8122662
            getSkinnable().requestLayout();
        };
        weakTabsListener = new WeakListChangeListener<>(tabsListener);
        getSkinnable().getTabs().addListener(weakTabsListener);
    }

    private void addTabContent(Tab tab) {
        TabContentRegion tabContentRegion = new TabContentRegion(tab);
        tabContentRegion.setClip(new Rectangle());
        tabContentRegions.add(tabContentRegion);
        // We want the tab content to always sit below the tab headers
        getChildren().add(0, tabContentRegion);
    }

    private void removeTabContent(Tab tab) {
        for (TabContentRegion contentRegion : tabContentRegions) {
            if (contentRegion.getTab().equals(tab)) {
                removeTabContent(contentRegion);
                break;
            }
        }
    }

    private void removeTabContent(TabContentRegion contentRegion) {
        contentRegion.dispose();
        tabContentRegions.remove(contentRegion);
        getChildren().remove(contentRegion);
    }

    private void updateTabPosition() {
        tabHeaderArea.invalidateScrollOffset();
        getSkinnable().applyCss();
        getSkinnable().requestLayout();
    }

    private Timeline createTimeline(final TabHeaderSkin tabRegion, final Duration duration, final double endValue, final EventHandler<ActionEvent> func) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue keyValue = new KeyValue(tabRegion.animationTransition, endValue, Interpolator.LINEAR);
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(new KeyFrame(duration, keyValue));

        timeline.setOnFinished(func);
        return timeline;
    }

    private boolean isHorizontal() {
        Side tabPosition = getSkinnable().getSide();
        return Side.TOP.equals(tabPosition) || Side.BOTTOM.equals(tabPosition);
    }

    private void initializeSwipeHandlers() {
        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            getSkinnable().addEventHandler(SwipeEvent.SWIPE_LEFT, t -> {
                behavior.selectNextTab();
            });

            getSkinnable().addEventHandler(SwipeEvent.SWIPE_RIGHT, t -> {
                behavior.selectPreviousTab();
            });
        }
    }

    //TODO need to cache this.
    private boolean isFloatingStyleClass() {
        return getSkinnable().getStyleClass().contains(TabPane.STYLE_CLASS_FLOATING);
    }



    /* *************************************************************************
     *                                                                         *
     * CSS                                                                     *
     *                                                                         *
     **************************************************************************/

   /*
    * Super-lazy instantiation pattern from Bill Pugh.
    */
   private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private final static CssMetaData<TabPane,TabAnimation> OPEN_TAB_ANIMATION =
                new CssMetaData<>("-fx-open-tab-animation",
                    new EnumConverter<>(TabAnimation.class), TabAnimation.GROW) {

            @Override public boolean isSettable(TabPane node) {
                return true;
            }

            @Override public StyleableProperty<TabAnimation> getStyleableProperty(TabPane node) {
                TabPaneProSkin skin = (TabPaneProSkin) node.getSkin();
                return (StyleableProperty<TabAnimation>)(WritableValue<TabAnimation>)skin.openTabAnimation;
            }
        };

        private final static CssMetaData<TabPane,TabAnimation> CLOSE_TAB_ANIMATION =
                new CssMetaData<>("-fx-close-tab-animation",
                    new EnumConverter<>(TabAnimation.class), TabAnimation.GROW) {

            @Override public boolean isSettable(TabPane node) {
                return true;
            }

            @Override public StyleableProperty<TabAnimation> getStyleableProperty(TabPane node) {
                TabPaneProSkin skin = (TabPaneProSkin) node.getSkin();
                return (StyleableProperty<TabAnimation>)(WritableValue<TabAnimation>)skin.closeTabAnimation;
            }
        };

        static {

           final List<CssMetaData<? extends Styleable, ?>> styleables =
               new ArrayList<>(SkinBase.getClassCssMetaData());
           styleables.add(OPEN_TAB_ANIMATION);
           styleables.add(CLOSE_TAB_ANIMATION);
           STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    /**
     * Returns the CssMetaData associated with this class, which may include the
     * CssMetaData of its superclasses.
     * @return the CssMetaData associated with this class, which may include the
     * CssMetaData of its superclasses
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }



    /* *************************************************************************
     *                                                                         *
     * Support classes                                                         *
     *                                                                         *
     **************************************************************************/

    /* ************************************************************************
     *
     * TabHeaderArea: Area responsible for painting all tabs
     *
     **************************************************************************/
    class TabHeaderArea extends StackPane {
        private Rectangle headerClip;
        private StackPane headersRegion;
        private StackPane headerBackground;
        private TabsMenuManager tabsMenuManager;

        private boolean measureClosingTabs = false;

        private final ReadOnlyDoubleWrapper scrollOffset = new ReadOnlyDoubleWrapper();

        private boolean scrollOffsetDirty = true;

        private final StackPane headerFirstArea = new StackPane();

        private final StackPane headerLastArea = new StackPane();

        private final StackPane headerStickyArea = new StackPane();

        private final ReadOnlyBooleanWrapper headersRegionOverflowed = new ReadOnlyBooleanWrapper(false);

        private boolean dummyTabAdded = false;

        /**
        * The ScrollBar is added to the header when the user enables it, and removed when the user disables it.
        * Its visibility depends on three factors:
        * 1) the mouse is hovering over the header,
        * 2) the user is currently scrolling via the thumb,
        * 3) whether all tabs fit within the available space.
        */
        private final TabScrollBar scrollBar = new TabScrollBar();

        private final BooleanProperty scrollBarEnabled = new SimpleBooleanProperty(false);

        private Node scrollBarThumb;

        private boolean scrollingViaThumb;

        private boolean mouseIsOverHeaderClip;

        private boolean scrollBarListenerEnabled = true;

        private FadeTransition showTransition;

        private FadeTransition hideTransition;

        private final ObjectProperty<TabHeaderAreaPolicy> policy =
                new SimpleObjectProperty<>(TabHeaderAreaPolicy.VISIBLE_WHEN_TABS_PRESENT);

        public TabHeaderArea() {
            getStyleClass().setAll("tab-header-area");
            setManaged(false);
            final TabPanePro tabPane = getSkinnable();

            headerClip = new Rectangle();

            headersRegion = new StackPane() {
                @Override protected double computePrefWidth(double height) {
                    double width = 0.0F;
                    for (Node child : getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)child;
                        if (tabHeaderSkin.isVisible() && (measureClosingTabs || ! tabHeaderSkin.isClosing)) {
                            width += tabHeaderSkin.prefWidth(height);
                        }
                    }
                    return snapSizeX(width) + snappedLeftInset() + snappedRightInset();
                }

                @Override protected double computePrefHeight(double width) {
                    double height = 0.0F;
                    for (Node child : getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)child;
                        height = Math.max(height, tabHeaderSkin.prefHeight(width));
                    }
                    return snapSizeY(height) + snappedTopInset() + snappedBottomInset();
                }

                @Override protected void layoutChildren() {
                    double firstAreaWidth = computeRegionWidth(headerFirstArea, -1);
                    double stickyAreaWidth = computeRegionWidth(headerStickyArea, -1);
                    double lastAreaWidth = computeRegionWidth(headerLastArea, -1);

                    if (tabsFit(firstAreaWidth, stickyAreaWidth, lastAreaWidth)) {
                        setScrollOffset(0.0, firstAreaWidth, stickyAreaWidth, lastAreaWidth);
                    } else {
                        if (scrollOffsetDirty) {
                            ensureSelectedTabIsVisible(firstAreaWidth, stickyAreaWidth, lastAreaWidth);
                            scrollOffsetDirty = false;
                        }
                        // ensure there's no gap between last visible tab and trailing edge
                        validateScrollOffset(firstAreaWidth, stickyAreaWidth, lastAreaWidth);
                    }

                    Side tabPosition = getSkinnable().getSide();
                    double tabBackgroundHeight = snapSizeY(prefHeight(-1));
                    double tabX = (tabPosition.equals(Side.LEFT) || tabPosition.equals(Side.BOTTOM)) ?
                        snapSizeX(getWidth()) - getScrollOffset() : getScrollOffset();

                    updateHeaderClip(firstAreaWidth, stickyAreaWidth, lastAreaWidth);
                    for (Node node : getChildren()) {
                        TabHeaderSkin tabHeader = (TabHeaderSkin)node;

                        // size and position the header relative to the other headers
                        double tabHeaderPrefWidth = snapSizeX(tabHeader.prefWidth(-1) * tabHeader.animationTransition.get());
                        double tabHeaderPrefHeight = snapSizeY(tabHeader.prefHeight(-1));
                        tabHeader.resize(tabHeaderPrefWidth, tabHeaderPrefHeight);

                        // This ensures that the tabs are located in the correct position
                        // when there are tabs of differing heights.
                        double startY = tabPosition.equals(Side.BOTTOM) ?
                            0 : tabBackgroundHeight - tabHeaderPrefHeight - snappedBottomInset();
                        if (tabPosition.equals(Side.LEFT) || tabPosition.equals(Side.BOTTOM)) {
                            // build from the right
                            tabX -= tabHeaderPrefWidth;
                            if (dragState != DragState.REORDER ||
                                    (tabHeader != dragTabHeader && tabHeader != dropAnimHeader)) {
                                tabHeader.relocate(tabX, startY);
                            }
                        } else {
                            // build from the left
                            if (dragState != DragState.REORDER ||
                                    (tabHeader != dragTabHeader && tabHeader != dropAnimHeader)) {
                                tabHeader.relocate(tabX, startY);
                            }
                            tabX += tabHeaderPrefWidth;
                        }
                    }
                }

            };
            headersRegion.getStyleClass().setAll("headers-region");
            headersRegion.setClip(headerClip);
            setupReordering(headersRegion);

            headerBackground = new StackPane();
            headerBackground.getStyleClass().setAll("tab-header-background");
            // when using the Classic style with AtlantaFX, you can't press
            // any buttons in the first/last area because of the header background
            headerBackground.setMouseTransparent(true);

            int i = 0;
            for (Tab tab: tabPane.getTabs()) {
                addTab(tab, i++);
            }

            tabsMenuManager = new TabsMenuManager();
            getChildren().addAll(headerBackground, headersRegion, headerStickyArea, headerFirstArea, headerLastArea);

            // support for mouse scroll of header area (for when the tabs exceed
            // the available space).
            // Scrolling the mouse wheel downwards results in the tabs scrolling left (i.e. exposing the right-most tabs)
            // Scrolling the mouse wheel upwards results in the tabs scrolling right (i.e. exposing th left-most tabs)
            addEventHandler(ScrollEvent.SCROLL, (ScrollEvent e) -> {
                double dx = e.getDeltaX();
                double dy = e.getDeltaY();

                Side side = getSkinnable().getSide();
                side = side == null ? Side.TOP : side;
                switch (side) {
                    default:
                    case TOP:
                    case BOTTOM:
                        // Consider vertical scroll events (dy > dx) from mouse wheel and trackpad,
                        // and horizontal scroll events from a trackpad (dx > dy)
                        dx = Math.abs(dy) > Math.abs(dx) ? dy : dx;
                        scrollTabsBy(dx);
                        break;
                    case LEFT:
                    case RIGHT:
                        scrollTabsBy(dy * -1);
                        break;
                }
            });

            this.headerFirstArea.getStyleClass().add("first-area");
            this.headerFirstArea.setViewOrder(-10);
            this.headerLastArea.getStyleClass().add("last-area");
            this.headerLastArea.setViewOrder(-10);
            this.headerStickyArea.getStyleClass().add("sticky-area");
            this.headerStickyArea.setViewOrder(-9);
            this.scrollBar.setUnitIncrement(10);
            this.scrollBar.setBlockIncrement(25);
            this.scrollBar.setViewOrder(-8);
            this.scrollBar.setVisible(false);
            this.scrollBar.setMin(0);
            this.scrollBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (scrollBarListenerEnabled) {
                    var delta = newVal.doubleValue() - oldVal.doubleValue();
                    scrollTabsBy(delta * -1);
                }
            });
            updateElementsRotation();

            addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                if (this.scrollBar.getParent() != null) {
                    updateMousePosition(isMouseOverHeaderClip(e));
                }
            });
            addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                if (this.scrollBar.getParent() != null) {
                    updateMousePosition(false);
                }
            });

            updateNoTabsState();
            policy.addListener((ov, oldV, newV) -> updateNoTabsState());
            updateScrollBarPresence();
            scrollBarEnabled.addListener((ov, oldV, newV) -> updateScrollBarPresence());
        }

        private boolean isMouseOverHeaderClip(MouseEvent e) {
            var mouseIsOver = false;
            var side = getSkinnable().getSide();
            if (side == TOP || side == RIGHT) {
                if (e.getX() >= headerFirstArea.getLayoutX() + headerFirstArea.getWidth()
                        && e.getX() <= headerStickyArea.getLayoutX()) {
                    mouseIsOver = true;
                }
            } else if (side == BOTTOM) {
                if (e.getX() >= headerStickyArea.getLayoutX() + headerStickyArea.getWidth()
                        && e.getX() <= headerFirstArea.getLayoutX()) {
                    mouseIsOver = true;
                }
            } else if (side == LEFT) {
                if (e.getX() <= headerFirstArea.getLayoutX()
                        && e.getX() >= headerStickyArea.getLayoutX() + headerStickyArea.getWidth()) {
                    mouseIsOver = true;
                }
            }
            return mouseIsOver;
        }

        private void updateMousePosition(boolean mouseIsOver) {
            if (this.mouseIsOverHeaderClip) {
                if (!mouseIsOver) {
                    this.mouseIsOverHeaderClip = mouseIsOver;
                    if (!scrollingViaThumb) {
                        requestLayout();
                    }
                }
            } else {
                if (mouseIsOver) {
                    this.mouseIsOverHeaderClip = mouseIsOver;
                    requestLayout();
                }
            }
        }

        private void scrollTabsBy(double delta) {
            var firstAreaWidth = snapSizeX(headerFirstArea.getWidth());
            var stickyAreaWidth = snapSizeX(headerStickyArea.getWidth());
            var lastAreaWidth = snapSizeX(headerLastArea.getWidth());
            // it is important to snap the delta
            delta = getSkinnable().getSide().isHorizontal() ? snapSizeX(delta) : snapSizeY(delta);
            setScrollOffset(scrollOffset.get() + delta, firstAreaWidth, stickyAreaWidth, lastAreaWidth);
        }

        private void updateHeaderClip(double firstAreaWidth, double stickyAreaWidth, double lastAreaWidth) {
            Side tabPosition = getSkinnable().getSide();

            double x = 0;
            double y = 0;
            double clipWidth = 0;
            double clipHeight = 0;
            double maxWidth = 0;
            double shadowRadius = 0;
            double clipOffset = firstTabIndent() + lastTabIndent();

            measureClosingTabs = true;
            double headersPrefWidth = snapSizeX(headersRegion.prefWidth(-1));
            measureClosingTabs = false;

            double headersPrefHeight = snapSizeY(headersRegion.prefHeight(-1));

            if (headersRegion.getEffect() instanceof DropShadow) {
                DropShadow shadow = (DropShadow)headersRegion.getEffect();
                shadowRadius = shadow.getRadius();
            }

            maxWidth = snapSizeX(getWidth()) - firstAreaWidth - stickyAreaWidth - lastAreaWidth - clipOffset;
            if (tabPosition.equals(Side.LEFT) || tabPosition.equals(Side.BOTTOM)) {
                if (headersPrefWidth < maxWidth) {
                    clipWidth = headersPrefWidth + shadowRadius;
                } else {
                    x = headersPrefWidth - maxWidth;
                    clipWidth = maxWidth + shadowRadius;
                }
                clipHeight = headersPrefHeight;
            } else {
                // If x = 0 the header region's drop shadow is clipped.
                x = -shadowRadius;
                clipWidth = (headersPrefWidth < maxWidth ? headersPrefWidth : maxWidth) + shadowRadius;
                clipHeight = headersPrefHeight;
            }

            headerClip.setX(x);
            headerClip.setY(y);
            headerClip.setWidth(clipWidth);
            headerClip.setHeight(clipHeight);
        }

        private void addTab(Tab tab, int addToIndex) {
            TabHeaderSkin tabHeaderSkin = new TabHeaderSkin(tab);
            headersRegion.getChildren().add(addToIndex, tabHeaderSkin);
            invalidateScrollOffset();
        }

        private void removeTab(Tab tab) {
            TabHeaderSkin tabHeaderSkin = getTabHeaderSkin(tab);
            if (tabHeaderSkin != null) {
                headersRegion.getChildren().remove(tabHeaderSkin);
            }
            invalidateScrollOffset();
        }

        private void moveTab(int moveToIndex, TabHeaderSkin tabHeaderSkin) {
            if (moveToIndex != headersRegion.getChildren().indexOf(tabHeaderSkin)) {
                headersRegion.getChildren().remove(tabHeaderSkin);
                headersRegion.getChildren().add(moveToIndex, tabHeaderSkin);
            }
            invalidateScrollOffset();
        }

        private TabHeaderSkin getTabHeaderSkin(Tab tab) {
            for (Node child: headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin)child;
                if (tabHeaderSkin.getTab().equals(tab)) {
                    return tabHeaderSkin;
                }
            }
            return null;
        }

        private boolean tabsFit(double firstAreaWidth, double stickyAreaWidth, double lastAreaWidth) {
            double headerPrefWidth = snapSizeX(headersRegion.prefWidth(-1));
            double visibleWidth = firstTabIndent() + firstAreaWidth + headerPrefWidth + stickyAreaWidth
                    + lastAreaWidth + lastTabIndent();
            var result = visibleWidth < getWidth();
            headersRegionOverflowed.set(!result);
            return result;
        }

        private void ensureSelectedTabIsVisible(double firstAreaWidth, double stickyAreaWidth, double lastAreaWidth) {
            // work out the visible width of the tab header
            double tabPaneWidth = snapSizeX(isHorizontal() ? getSkinnable().getWidth() : getSkinnable().getHeight());
            //double controlTabWidth = snapSizeX(controlButtons.getWidth()); getWidth???
            double visibleWidth = tabPaneWidth - firstTabIndent() - firstAreaWidth - stickyAreaWidth
                    - lastAreaWidth - lastTabIndent();

            // and get where the selected tab is in the header area
            double offset = 0.0;
            double selectedTabOffset = 0.0;
            double selectedTabWidth = 0.0;
            for (Node node : headersRegion.getChildren()) {
                TabHeaderSkin tabHeader = (TabHeaderSkin)node;

                double tabHeaderPrefWidth = snapSizeX(tabHeader.prefWidth(-1));

                if (selectedTab != null && selectedTab.equals(tabHeader.getTab())) {
                    selectedTabOffset = offset;
                    selectedTabWidth = tabHeaderPrefWidth;
                }
                offset += tabHeaderPrefWidth;
            }

            final double scrollOffset = getScrollOffset();
            final double selectedTabStartX = selectedTabOffset;
            final double selectedTabEndX = selectedTabOffset + selectedTabWidth;

            final double visibleAreaEndX = visibleWidth;

            if (selectedTabStartX < -scrollOffset) {
                setScrollOffset(-selectedTabStartX, firstAreaWidth, stickyAreaWidth, lastAreaWidth);
            } else if (selectedTabEndX > (visibleAreaEndX - scrollOffset)) {
                setScrollOffset(visibleAreaEndX - selectedTabEndX, firstAreaWidth, stickyAreaWidth, lastAreaWidth);
            }
        }

        public double getScrollOffset() {
            return scrollOffset.get();
        }

        public void invalidateScrollOffset() {
            scrollOffsetDirty = true;
        }

        private void validateScrollOffset(double firstAreaWidth, double stickyAreaWidth, double lastAreaWidth) {
            setScrollOffset(getScrollOffset(), firstAreaWidth, stickyAreaWidth, lastAreaWidth);
        }

        private void setScrollOffset(double newScrollOffset, double firstAreaWidth, double stickyAreaWidth,
                double lastAreaWidth) {
            // work out the visible width of the tab header
            double tabPaneWidth = snapSizeX(isHorizontal() ? getSkinnable().getWidth() : getSkinnable().getHeight());
            double visibleWidth = tabPaneWidth - firstTabIndent() - firstAreaWidth - stickyAreaWidth
                    - lastAreaWidth - lastTabIndent();

            // measure the width of all tabs
            double offset = 0.0;
            for (Node node : headersRegion.getChildren()) {
                TabHeaderSkin tabHeader = (TabHeaderSkin)node;
                double tabHeaderPrefWidth = snapSizeX(tabHeader.prefWidth(-1));
                offset += tabHeaderPrefWidth;
            }

            double actualNewScrollOffset;

            if ((visibleWidth - newScrollOffset) > offset && newScrollOffset < 0) {
                // need to make sure the right-most tab is attached to the
                // right-hand side of the tab header (e.g. if the tab header area width
                // is expanded), and if it isn't modify the scroll offset to bring
                // it into line. See JDK-8095332 for a test case.
                actualNewScrollOffset = visibleWidth - offset;
            } else if (newScrollOffset > 0) {
                // need to prevent the left-most tab from becoming detached
                // from the left-hand side of the tab header.
                actualNewScrollOffset = 0;
            } else {
                actualNewScrollOffset = newScrollOffset;
            }

            if (Math.abs(actualNewScrollOffset - scrollOffset.get()) > 0.001) {
                scrollOffset.set(actualNewScrollOffset);
                headersRegion.requestLayout();
            }
        }

        private double firstTabIndent() {
            switch (getSkinnable().getSide()) {
                case TOP:
                case BOTTOM:
                    return snappedLeftInset();
                case RIGHT:
                case LEFT:
                    return snappedTopInset();
                default:
                    return 0;
            }
        }

        private double lastTabIndent() {
            switch (getSkinnable().getSide()) {
                case TOP:
                case BOTTOM:
                    return snappedRightInset();
                case RIGHT:
                case LEFT:
                    return snappedBottomInset();
                default:
                    return 0;
            }
        }

        @Override protected double computePrefWidth(double height) {
            double padding = isHorizontal() ?
                snappedLeftInset() + snappedRightInset() :
                snappedTopInset() + snappedBottomInset();
            return computeRegionWidth(headerFirstArea, height)
                    + snapSizeX(headersRegion.prefWidth(height))
                    + computeRegionWidth(headerStickyArea, height)
                    + computeRegionWidth(headerLastArea, height)
                    + padding; // + firstTabIndent() ??
        }

        @Override protected double computePrefHeight(double width) {
            double padding = isHorizontal() ?
                snappedTopInset() + snappedBottomInset() :
                snappedLeftInset() + snappedRightInset();
            var firstAreaHeight = computeRegionHeight(headerFirstArea, width);
            var lastAreaHeight = computeRegionHeight(headerLastArea, width);
            var stickyAreaHeight = computeRegionHeight(headerStickyArea, width);
            var max = Math.max(firstAreaHeight, lastAreaHeight);
            max = Math.max(max, stickyAreaHeight);
            max = Math.max(max, snapSizeY(headersRegion.prefHeight(-1)));
            return max + padding;
        }

        @Override public double getBaselineOffset() {
            if (getSkinnable().getSide() == Side.TOP) {
                return headersRegion.getBaselineOffset() + snappedTopInset();
            }
            return 0;
        }

        @Override protected void layoutChildren() {
            final double leftInset = snappedLeftInset();
            final double rightInset = snappedRightInset();
            final double topInset = snappedTopInset();
            final double bottomInset = snappedBottomInset();

            double headerWidth = snapSizeX(getWidth());
            double headerHeight = snapSizeY(getHeight());

            double w = headerWidth - (isHorizontal() ?
                    leftInset + rightInset : topInset + bottomInset);
            double h = headerHeight - (isHorizontal() ?
                    topInset + bottomInset : leftInset + rightInset);
            double tabBackgroundHeight = snapSizeY(prefHeight(-1));
            double headersPrefWidth = snapSizeX(headersRegion.prefWidth(-1));
            double headersPrefHeight = snapSizeY(headersRegion.prefHeight(-1));

            var firstAreaWidth = computeRegionWidth(headerFirstArea, -1);
            var firstAreaHeight = computeRegionHeight(headerFirstArea, -1);
            firstAreaHeight = Math.max(firstAreaHeight, h);
            headerFirstArea.resize(firstAreaWidth, firstAreaHeight);

            var stickyAreaWidth = computeRegionWidth(headerStickyArea, -1);
            var stickyAreaHeight = computeRegionHeight(headerStickyArea, -1);
            stickyAreaHeight = Math.max(stickyAreaHeight, h);
            headerStickyArea.resize(stickyAreaWidth, stickyAreaHeight);

            var lastAreaWidth = computeRegionWidth(headerLastArea, -1);
            var lastAreaHeight = computeRegionHeight(headerLastArea, -1);
            lastAreaHeight = Math.max(lastAreaHeight, h);
            headerLastArea.resize(lastAreaWidth, lastAreaHeight);

            double scrollBarWidth = 0.0;
            double scrollBarHeight = 0.0;
            if (scrollBar.getParent() != null) {
                scrollBarWidth = w - firstAreaWidth - stickyAreaWidth - lastAreaWidth;
                scrollBarHeight = computeRegionHeight(scrollBar, -1);
                scrollBar.resize(scrollBarWidth, scrollBarHeight);
            }

            h = Math.min(h, headersPrefHeight);

            updateHeaderClip(firstAreaWidth, stickyAreaWidth, lastAreaWidth);
            headersRegion.requestLayout();

            // POSITION TABS
            headersRegion.resize(headersPrefWidth, headersPrefHeight);

            if (isFloatingStyleClass()) {
                headerBackground.setVisible(false);
            } else {
                headerBackground.resize(headerWidth, headerHeight);
                headerBackground.setVisible(true);
            }

            double allAreaY = 0;
            double firstAreaX = 0;
            double regionX = 0;
            double regionY = 0;
            double stickyX = 0;
            double lastAreaX = 0;
            Side tabPosition = getSkinnable().getSide();
            var tabsFit = tabsFit(firstAreaWidth, stickyAreaWidth, lastAreaWidth);
            double scrollBarX = 0;
            double scrollBarY = 0;

            if (tabPosition.equals(Side.TOP)) {
                firstAreaX = leftInset;
                allAreaY = topInset;
                regionX = firstAreaX + firstAreaWidth;
                regionY = tabBackgroundHeight - headersPrefHeight - bottomInset;
                lastAreaX = w - lastAreaWidth + leftInset;
                if (isDummyTabAdded()) {
                    stickyX = regionX;
                } else {
                    if (tabsFit) {
                        stickyX = regionX + headersPrefWidth;
                    } else {
                        stickyX = lastAreaX - stickyAreaWidth;
                    }
                }
                scrollBarX = regionX;
                if (this.scrollBar.getHeaderPosition() == TabScrollBar.HeaderPosition.ABOVE_TABS) {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = 0;
                    } else {
                        scrollBarY = topInset;
                    }
                } else {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = headerHeight - scrollBarHeight;
                    } else {
                        scrollBarY = topInset + firstAreaHeight - scrollBarHeight;
                    }
                }
            } else if (tabPosition.equals(Side.RIGHT)) {
                firstAreaX = topInset;
                allAreaY = tabBackgroundHeight - firstAreaHeight - leftInset;
                regionX = firstAreaX + firstAreaWidth;
                regionY = tabBackgroundHeight - headersPrefHeight - leftInset;
                lastAreaX = w - lastAreaWidth + topInset;
                if (isDummyTabAdded()) {
                    stickyX = regionX;
                } else {
                    if (tabsFit) {
                        stickyX = regionX + headersPrefWidth;
                    } else {
                        stickyX = lastAreaX - stickyAreaWidth;
                    }
                }
                scrollBarX = regionX;
                if (this.scrollBar.getHeaderPosition() == TabScrollBar.HeaderPosition.ABOVE_TABS) {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = 0;
                    } else {
                        scrollBarY = rightInset;
                    }
                } else {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = headerHeight - scrollBarHeight;
                    } else {
                        scrollBarY = headerHeight - leftInset - scrollBarHeight;
                    }
                }
            } else if (tabPosition.equals(Side.BOTTOM)) {
                firstAreaX = headerWidth - firstAreaWidth - leftInset;
                allAreaY = tabBackgroundHeight - firstAreaHeight - topInset;
                regionX = headerWidth - headersPrefWidth - firstAreaWidth - leftInset;
                regionY = tabBackgroundHeight - headersPrefHeight - topInset;
                lastAreaX = rightInset;
                if (isDummyTabAdded()) {
                    stickyX = regionX;
                } else {
                    if (tabsFit) {
                        stickyX = regionX - stickyAreaWidth;
                    } else {
                        stickyX = lastAreaX + lastAreaWidth;
                    }
                }
                scrollBarX = lastAreaX + lastAreaWidth + stickyAreaWidth;
                if (this.scrollBar.getHeaderPosition() == TabScrollBar.HeaderPosition.ABOVE_TABS) {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = tabBackgroundHeight - scrollBarHeight;
                    } else {
                        scrollBarY = tabBackgroundHeight - scrollBarHeight - topInset;
                    }
                } else {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = 0;
                    } else {
                        scrollBarY = topInset;
                    }
                }
            } else if (tabPosition.equals(Side.LEFT)) {
                firstAreaX = headerWidth - firstAreaWidth - topInset;
                allAreaY = tabBackgroundHeight - firstAreaHeight - rightInset;
                regionX = headerWidth - headersPrefWidth - firstAreaWidth - topInset;
                regionY = tabBackgroundHeight - headersPrefHeight - rightInset;
                lastAreaX = bottomInset;
                if (isDummyTabAdded()) {
                    stickyX = regionX;
                } else {
                    if (tabsFit) {
                        stickyX = regionX - stickyAreaWidth;
                    } else {
                        stickyX = lastAreaX + lastAreaWidth;
                    }
                }
                scrollBarX = firstAreaX - scrollBarWidth;
                if (this.scrollBar.getHeaderPosition() == TabScrollBar.HeaderPosition.ABOVE_TABS) {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = 0;
                    } else {
                        scrollBarY = leftInset;
                    }
                } else {
                    if (this.scrollBar.isStickToEdge()) {
                        scrollBarY = headerHeight - scrollBarHeight;
                    } else {
                        scrollBarY = headerHeight - scrollBarHeight - rightInset;
                    }
                }
            }
            if (headerBackground.isVisible()) {
                positionInArea(headerBackground, 0, 0,
                        headerWidth, headerHeight, /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
            }

            positionInArea(headerFirstArea, firstAreaX, allAreaY, firstAreaWidth, firstAreaHeight,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
            positionInArea(headersRegion, regionX, regionY, w, h, /*baseline ignored*/0, HPos.LEFT, VPos.CENTER);
            positionInArea(headerStickyArea, stickyX, allAreaY, stickyAreaWidth, stickyAreaHeight,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
            positionInArea(headerLastArea, lastAreaX, allAreaY, lastAreaWidth, lastAreaHeight,
                    /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
            if (scrollBar.getParent() != null) {
                // position in any case - visible or not
                positionInArea(scrollBar, scrollBarX, scrollBarY, scrollBarWidth,
                        scrollBarHeight,  /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
                initScrollBarThumb();
                if (scrollBar.isVisible()) {
                    if (tabsFit) {
                        // in this case, animation is not used because the width of the ScrollBar exceeds the
                        // bounds of the visible tabs, resulting in an undesirable visual effect.
                        hideScrollBar(false);
                    } else if (!mouseIsOverHeaderClip && !scrollingViaThumb) {
                        hideScrollBar(true);
                    }
                    updateScrollBarMetrics(scrollBarWidth, headersPrefWidth);
                } else {
                    if (!tabsFit && mouseIsOverHeaderClip) {
                        updateScrollBarMetrics(scrollBarWidth, headersPrefWidth);
                        showScrollBar();
                    }
                }
            }
        }

        void dispose() {
            for (Node child : headersRegion.getChildren()) {
                TabHeaderSkin header = (TabHeaderSkin) child;
                header.dispose();
            }
            tabsMenuManager.dispose();
        }

        private void updateScrollBarPresence() {
            if (scrollBarEnabled.get()) {
                if (this.scrollBar.getParent() == null) {
                    getChildren().add(this.scrollBar);
                }
            } else {
                if (this.scrollBar.getParent() != null) {
                    getChildren().remove(this.scrollBar);
                }
            }
        }

        private void showScrollBar() {
            if (scrollBar.getParent() != null && !this.scrollBar.isVisible() && showTransition == null) {
                showTransition = new FadeTransition(Duration.millis(ANIMATION_SPEED * 2), scrollBar);
                this.scrollBar.setVisible(true);
                this.scrollBar.setOpacity(0);
                showTransition.setToValue(1);
                showTransition.setOnFinished(e -> showTransition = null);
                showTransition.play();
            }
        }

        private void hideScrollBar(boolean animationEnabled) {
            if (!this.scrollBar.isVisible()) {
                return;
            }
            if (animationEnabled) {
                if (hideTransition == null) {
                    hideTransition = new FadeTransition(Duration.millis(ANIMATION_SPEED * 2), scrollBar);
                    hideTransition.setToValue(0);
                    hideTransition.setOnFinished((e) -> {
                        this.scrollBar.setVisible(false);
                        hideTransition = null;
                    });
                    hideTransition.play();
                }
            } else {
                this.scrollBar.setVisible(false);
            }
        }

        private void updateScrollBarMetrics(double scrollBarWidth, double regionWidth) {
            this.scrollBarListenerEnabled = false;
            double max = Math.max(0, regionWidth - scrollBarWidth);
            scrollBar.setMax(max);
            double visibleAmount = (scrollBarWidth / regionWidth) * max;
            scrollBar.setVisibleAmount(visibleAmount);
            scrollBar.setValue(Math.min(scrollOffset.get() * -1, max));
            this.scrollBarListenerEnabled = true;
        }

        private void initScrollBarThumb() {
            if (scrollBarThumb == null && scrollBar.getScene() != null) {
                scrollBarThumb = scrollBar.lookup(".thumb");
                if (scrollBarThumb != null) {
                    scrollBarThumb.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> scrollingViaThumb = true);
                    scrollBarThumb.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                        scrollingViaThumb = false;
                        if (!mouseIsOverHeaderClip) {
                            requestLayout();
                        }
                    });
                }
            }
        }

        private double computeRegionWidth(Region region, double height) {
            var areaWidth = Utils.computeBoundedSize(
                    region.minWidth(height),
                    region.prefWidth(height),
                    region.maxWidth(height));
            areaWidth = snapSizeX(areaWidth);
            return areaWidth;
        }

        private double computeRegionHeight(Region region, double width) {
            var areaHeight = Utils.computeBoundedSize(
                    region.minHeight(width),
                    region.prefHeight(width),
                    region.maxHeight(width));
            areaHeight = snapSizeY(areaHeight);
            return areaHeight;
        }

        private boolean isDummyTabAdded() {
            return dummyTabAdded;
        }

        /**
        * When there are no tabs, it is necessary to know the height of the headersRegion in order to calculate the
        * correct height of the header area. This is achieved using a dummy tab header.
        */
        private void updateNoTabsState() {
            if (getSkinnable().getTabs().isEmpty()) {
                if (policy.get() == TabHeaderAreaPolicy.ALWAYS_VISIBLE) {
                    setVisible(true);
                    if (!dummyTabAdded) {
                        addDummyTab();
                    }
                } else {
                    setVisible(false);
                    if (dummyTabAdded) {
                        removeDummyTab();
                    }
                }
            } else {
                if (!isVisible()) {
                    setVisible(true);
                }
                if (dummyTabAdded) {
                    removeDummyTab();
                }
            }
        }

        private void addDummyTab() {
            var dummyTab = new Tab();
            var dummyTabSkin = new TabHeaderSkin(dummyTab);
            headersRegion.getChildren().add(dummyTabSkin);
            headersRegion.setVisible(false);
            dummyTabAdded = true;
        }

        private void removeDummyTab() {
            headersRegion.getChildren().clear();
            headersRegion.setVisible(true);
            dummyTabAdded = false;
        }

        private void updateElementsRotation() {
            var side = getSkinnable().getSide();
            switch (side) {
                case TOP:
                    scrollBar.setRotate(0);
                    break;
                case RIGHT:
                    scrollBar.setRotate(0);
                    break;
                case BOTTOM:
                    scrollBar.setRotate(180);
                    break;
                case LEFT:
                    scrollBar.setRotate(180);
                    break;
                default:
                    throw new AssertionError();
            }
        }

    } /* End TabHeaderArea */




    /* ************************************************************************
     *
     * TabHeaderSkin: skin for each tab
     *
     **************************************************************************/

    class TabHeaderSkin extends StackPane {
        private final Tab tab;
        public Tab getTab() {
            return tab;
        }
        private Label label;
        private StackPane closeBtn;
        private StackPane inner;
        private Tooltip oldTooltip;
        private Tooltip tooltip;
        private Rectangle clip;

        private boolean isClosing = false;

        private LambdaMultiplePropertyChangeListenerHandler listener = new LambdaMultiplePropertyChangeListenerHandler();

        private final ListChangeListener<String> styleClassListener = new ListChangeListener<>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                getStyleClass().setAll(tab.getStyleClass());
            }
        };

        private final WeakListChangeListener<String> weakStyleClassListener =
                new WeakListChangeListener<>(styleClassListener);

        public TabHeaderSkin(final Tab tab) {
            getStyleClass().setAll(tab.getStyleClass());
            setId(tab.getId());
            setStyle(tab.getStyle());
            setAccessibleRole(AccessibleRole.TAB_ITEM);
            setViewOrder(1);

            this.tab = tab;
            clip = new Rectangle();
            setClip(clip);

            label = new Label(tab.getText(), tab.getGraphic());
            label.getStyleClass().setAll("tab-label");

            closeBtn = new StackPane() {
                @Override protected double computePrefWidth(double h) {
                    return CLOSE_BTN_SIZE;
                }
                @Override protected double computePrefHeight(double w) {
                    return CLOSE_BTN_SIZE;
                }
                @Override
                public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
                    switch (action) {
                        case FIRE: {
                            Tab tab = getTab();
                            if (behavior.canCloseTab(tab)) {
                                behavior.closeTab(tab);
                                setOnMousePressed(null);
                            }
                            break;
                        }
                        default: super.executeAccessibleAction(action, parameters);
                    }
                }
            };
            closeBtn.setAccessibleRole(AccessibleRole.BUTTON);
            closeBtn.setAccessibleText(resourceBundle.getString("Accessibility.title.TabPane.CloseButton"));
            closeBtn.getStyleClass().setAll("tab-close-button");
            closeBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    Tab tab = getTab();
                    if (me.getButton().equals(MouseButton.PRIMARY) && behavior.canCloseTab(tab)) {
                        behavior.closeTab(tab);
                        setOnMousePressed(null);
                        me.consume();
                    }
                }
            });

            updateGraphicRotation();

            final Region focusIndicator = new Region();
            focusIndicator.setMouseTransparent(true);
            focusIndicator.getStyleClass().add("focus-indicator");

            inner = new StackPane() {
                @Override protected void layoutChildren() {
                    final TabPane skinnable = getSkinnable();

                    final double paddingTop = snappedTopInset();
                    final double paddingRight = snappedRightInset();
                    final double paddingBottom = snappedBottomInset();
                    final double paddingLeft = snappedLeftInset();
                    final double w = getWidth() - (paddingLeft + paddingRight);
                    final double h = getHeight() - (paddingTop + paddingBottom);

                    final double prefLabelWidth = snapSizeX(label.prefWidth(-1));
                    final double prefLabelHeight = snapSizeY(label.prefHeight(-1));

                    final double closeBtnWidth = showCloseButton() ? snapSizeX(closeBtn.prefWidth(-1)) : 0;
                    final double closeBtnHeight = showCloseButton() ? snapSizeY(closeBtn.prefHeight(-1)) : 0;
                    final double minWidth = snapSizeX(skinnable.getTabMinWidth());
                    final double maxWidth = snapSizeX(skinnable.getTabMaxWidth());
                    final double maxHeight = snapSizeY(skinnable.getTabMaxHeight());

                    double labelAreaWidth = prefLabelWidth;
                    double labelWidth = prefLabelWidth;
                    double labelHeight = prefLabelHeight;

                    final double childrenWidth = labelAreaWidth + closeBtnWidth;
                    final double childrenHeight = Math.max(labelHeight, closeBtnHeight);

                    if (childrenWidth > maxWidth && maxWidth != Double.MAX_VALUE) {
                        labelAreaWidth = maxWidth - closeBtnWidth;
                        labelWidth = maxWidth - closeBtnWidth;
                    } else if (childrenWidth < minWidth) {
                        labelAreaWidth = minWidth - closeBtnWidth;
                    }

                    if (childrenHeight > maxHeight && maxHeight != Double.MAX_VALUE) {
                        labelHeight = maxHeight;
                    }

                    if (animationState != TabAnimationState.NONE) {
//                        if (prefWidth.getValue() < labelAreaWidth) {
//                            labelAreaWidth = prefWidth.getValue();
//                        }
                        labelAreaWidth *= animationTransition.get();
                        closeBtn.setVisible(false);
                    } else {
                        closeBtn.setVisible(showCloseButton());
                    }


                    label.resize(labelWidth, labelHeight);


                    double labelStartX = paddingLeft;

                    // If maxWidth is less than Double.MAX_VALUE, the user has
                    // clamped the max width, but we should
                    // position the close button at the end of the tab,
                    // which may not necessarily be the entire width of the
                    // provided max width.
                    double closeBtnStartX = (maxWidth < Double.MAX_VALUE ? Math.min(w, maxWidth) : w) - paddingRight - closeBtnWidth;

                    positionInArea(label, labelStartX, paddingTop, labelAreaWidth, h,
                            /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);

                    if (closeBtn.isVisible()) {
                        closeBtn.resize(closeBtnWidth, closeBtnHeight);
                        positionInArea(closeBtn, closeBtnStartX, paddingTop, closeBtnWidth, h,
                                /*baseline ignored*/0, HPos.CENTER, VPos.CENTER);
                    }

                    // Magic numbers regretfully introduced for JDK-8124860 (so that
                    // the focus rect appears as expected on Windows and Mac).
                    // In short we use the vPadding to shift the focus rect down
                    // into the content area (whereas previously it was being clipped
                    // on Windows, whilst it still looked fine on Mac). In the
                    // future we may want to improve this code to remove the
                    // magic number. Similarly, the hPadding differs on Mac.
                    final int vPadding = Utils.isMac() ? 2 : 3;
                    final int hPadding = Utils.isMac() ? 2 : 1;
                    focusIndicator.resizeRelocate(
                            paddingLeft - hPadding,
                            paddingTop + vPadding,
                            w + 2 * hPadding,
                            h - 2 * vPadding);
                }
            };
            inner.getStyleClass().add("tab-container");
            inner.setRotate(getSkinnable().getSide().equals(Side.BOTTOM) ? 180.0F : 0.0F);
            inner.getChildren().addAll(label, closeBtn, focusIndicator);

            getChildren().addAll(inner);

            tooltip = tab.getTooltip();
            if (tooltip != null) {
                Tooltip.install(this, tooltip);
                oldTooltip = tooltip;
            }

            listener.registerChangeListener(tab.closableProperty(), e -> {
                inner.requestLayout();
                requestLayout();
            });
            listener.registerChangeListener(tab.selectedProperty(), e -> {
                pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, tab.isSelected());
                // Need to request a layout pass for inner because if the width
                // and height didn't not change the label or close button may have
                // changed.
                inner.requestLayout();
                requestLayout();
            });
            listener.registerChangeListener(tab.textProperty(),e -> label.setText(getTab().getText()));
            listener.registerChangeListener(tab.graphicProperty(), e -> label.setGraphic(getTab().getGraphic()));
            listener.registerChangeListener(tab.tooltipProperty(), e -> {
                // uninstall the old tooltip
                if (oldTooltip != null) {
                    Tooltip.uninstall(this, oldTooltip);
                }
                tooltip = tab.getTooltip();
                if (tooltip != null) {
                    // install new tooltip and save as old tooltip.
                    Tooltip.install(this, tooltip);
                    oldTooltip = tooltip;
                }
            });
            listener.registerChangeListener(tab.disabledProperty(), e -> {
                updateTabDisabledState();
            });
            if (tab.getTabPane() != null) {
                listener.registerChangeListener(tab.getTabPane().disabledProperty(), e -> {
                    updateTabDisabledState();
                });
            }
            listener.registerChangeListener(tab.styleProperty(), e -> setStyle(tab.getStyle()));

            tab.getStyleClass().addListener(weakStyleClassListener);

            listener.registerChangeListener(getSkinnable().tabClosingPolicyProperty(),e -> {
                inner.requestLayout();
                requestLayout();
            });
            listener.registerChangeListener(getSkinnable().sideProperty(),e -> {
                final Side side = getSkinnable().getSide();
                pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, (side == Side.TOP));
                pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, (side == Side.RIGHT));
                pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, (side == Side.BOTTOM));
                pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, (side == Side.LEFT));
                inner.setRotate(side == Side.BOTTOM ? 180.0F : 0.0F);
                if (getSkinnable().isRotateGraphic()) {
                    updateGraphicRotation();
                }
            });
            listener.registerChangeListener(getSkinnable().rotateGraphicProperty(), e -> updateGraphicRotation());
            listener.registerChangeListener(getSkinnable().tabMinWidthProperty(), e -> {
                requestLayout();
                getSkinnable().requestLayout();
            });
            listener.registerChangeListener(getSkinnable().tabMaxWidthProperty(), e -> {
                requestLayout();
                getSkinnable().requestLayout();
            });
            listener.registerChangeListener(getSkinnable().tabMinHeightProperty(), e -> {
                requestLayout();
                getSkinnable().requestLayout();
            });
            listener.registerChangeListener(getSkinnable().tabMaxHeightProperty(), e -> {
                requestLayout();
                getSkinnable().requestLayout();
            });

            getProperties().put(Tab.class, tab);
            getProperties().put(ContextMenu.class, tab.getContextMenu());

            setOnContextMenuRequested((ContextMenuEvent me) -> {
               if (getTab().getContextMenu() != null) {
                    getTab().getContextMenu().show(inner, me.getScreenX(), me.getScreenY());
                    me.consume();
                }
            });
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent me) {
                    Tab tab = getTab();
                    if (tab.isDisable()) {
                        return;
                    }
                    if (me.getButton().equals(MouseButton.MIDDLE)
                        || me.getButton().equals(MouseButton.PRIMARY)) {

                        if (tab.getContextMenu() != null
                            && tab.getContextMenu().isShowing()) {
                            tab.getContextMenu().hide();
                        }
                    }
                    if (me.getButton().equals(MouseButton.MIDDLE)) {
                        if (showCloseButton()) {
                            if (behavior.canCloseTab(tab)) {
                                dispose();
                                behavior.closeTab(tab);
                            }
                        }
                    } else if (me.getButton().equals(MouseButton.PRIMARY)) {
                        behavior.selectTab(tab);
                    }
                }
            });

            // initialize pseudo-class state
            pseudoClassStateChanged(SELECTED_PSEUDOCLASS_STATE, tab.isSelected());
            pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, tab.isDisabled());
            final Side side = getSkinnable().getSide();
            pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, (side == Side.TOP));
            pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, (side == Side.RIGHT));
            pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, (side == Side.BOTTOM));
            pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, (side == Side.LEFT));
        }

        private void updateTabDisabledState() {
            pseudoClassStateChanged(DISABLED_PSEUDOCLASS_STATE, tab.isDisabled());
            inner.requestLayout();
            requestLayout();
        }

        private void updateGraphicRotation() {
            if (label.getGraphic() != null) {
                label.getGraphic().setRotate(getSkinnable().isRotateGraphic() ? 0.0F :
                    (getSkinnable().getSide().equals(Side.RIGHT) ? -90.0F :
                        (getSkinnable().getSide().equals(Side.LEFT) ? 90.0F : 0.0F)));
            }
        }

        private boolean showCloseButton() {
            return tab.isClosable() &&
                    (getSkinnable().getTabClosingPolicy().equals(TabClosingPolicy.ALL_TABS) ||
                    getSkinnable().getTabClosingPolicy().equals(TabClosingPolicy.SELECTED_TAB) && tab.isSelected());
        }

        private final DoubleProperty animationTransition = new SimpleDoubleProperty(this, "animationTransition", 1.0) {
            @Override protected void invalidated() {
                requestLayout();
            }
        };

        private void dispose() {
            tab.getStyleClass().removeListener(weakStyleClassListener);
            listener.dispose();
            setOnContextMenuRequested(null);
            setOnMousePressed(null);
        }

        private TabAnimationState animationState = TabAnimationState.NONE;
        private Timeline currentAnimation;

        @Override protected double computePrefWidth(double height) {
//            if (animating) {
//                return prefWidth.getValue();
//            }
            double minWidth = snapSizeX(getSkinnable().getTabMinWidth());
            double maxWidth = snapSizeX(getSkinnable().getTabMaxWidth());
            double paddingRight = snappedRightInset();
            double paddingLeft = snappedLeftInset();
            double tmpPrefWidth = snapSizeX(label.prefWidth(-1));

            // only include the close button width if it is relevant
            if (showCloseButton()) {
                tmpPrefWidth += snapSizeX(closeBtn.prefWidth(-1));
            }

            if (tmpPrefWidth > maxWidth) {
                tmpPrefWidth = maxWidth;
            } else if (tmpPrefWidth < minWidth) {
                tmpPrefWidth = minWidth;
            }
            tmpPrefWidth += paddingRight + paddingLeft;
//            prefWidth.setValue(tmpPrefWidth);
            return tmpPrefWidth;
        }

        @Override protected double computePrefHeight(double width) {
            double minHeight = snapSizeY(getSkinnable().getTabMinHeight());
            double maxHeight = snapSizeY(getSkinnable().getTabMaxHeight());
            double paddingTop = snappedTopInset();
            double paddingBottom = snappedBottomInset();
            double tmpPrefHeight = snapSizeY(label.prefHeight(width));

            if (tmpPrefHeight > maxHeight) {
                tmpPrefHeight = maxHeight;
            } else if (tmpPrefHeight < minHeight) {
                tmpPrefHeight = minHeight;
            }
            tmpPrefHeight += paddingTop + paddingBottom;
            return tmpPrefHeight;
        }

        @Override protected void layoutChildren() {
            double w = (snapSizeX(getWidth()) - snappedRightInset() - snappedLeftInset()) * animationTransition.getValue();
            inner.resize(w, snapSizeY(getHeight()) - snappedTopInset() - snappedBottomInset());
            inner.relocate(snappedLeftInset(), snappedTopInset());
        }

        @Override protected void setWidth(double value) {
            super.setWidth(value);
            clip.setWidth(value);
        }

        @Override protected void setHeight(double value) {
            super.setHeight(value);
            clip.setHeight(value);
        }

        /** {@inheritDoc} */
        @Override
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case TEXT: return getTab().getText();
                case SELECTED: return selectedTab == getTab();
                default: return super.queryAccessibleAttribute(attribute, parameters);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
            switch (action) {
                case REQUEST_FOCUS:
                    getSkinnable().getSelectionModel().select(getTab());
                    break;
                default: super.executeAccessibleAction(action, parameters);
            }
        }

    } /* End TabHeaderSkin */

    private static final PseudoClass SELECTED_PSEUDOCLASS_STATE =
            PseudoClass.getPseudoClass("selected");
    private static final PseudoClass TOP_PSEUDOCLASS_STATE =
            PseudoClass.getPseudoClass("top");
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE =
            PseudoClass.getPseudoClass("bottom");
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE =
            PseudoClass.getPseudoClass("left");
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE =
            PseudoClass.getPseudoClass("right");
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE =
            PseudoClass.getPseudoClass("disabled");


    /* ************************************************************************
     *
     * TabContentRegion: each tab has one to contain the tab's content node
     *
     **************************************************************************/
    static class TabContentRegion extends StackPane {

        private Tab tab;

        private InvalidationListener tabContentListener = valueModel -> {
            updateContent();
        };
        private InvalidationListener tabSelectedListener = new InvalidationListener() {
            @Override public void invalidated(Observable valueModel) {
                setVisible(tab.isSelected());
            }
        };

        private WeakInvalidationListener weakTabContentListener =
                new WeakInvalidationListener(tabContentListener);
        private WeakInvalidationListener weakTabSelectedListener =
                new WeakInvalidationListener(tabSelectedListener);

        public Tab getTab() {
            return tab;
        }

        public TabContentRegion(Tab tab) {
            getStyleClass().setAll("tab-content-area");
            setManaged(false);
            this.tab = tab;
            updateContent();
            setVisible(tab.isSelected());

            tab.selectedProperty().addListener(weakTabSelectedListener);
            tab.contentProperty().addListener(weakTabContentListener);
        }

        private void updateContent() {
            Node newContent = getTab().getContent();
            if (newContent == null) {
                getChildren().clear();
            } else {
                getChildren().setAll(newContent);
            }
        }

        public void dispose() {
            tab.selectedProperty().removeListener(weakTabSelectedListener);
            tab.contentProperty().removeListener(weakTabContentListener);
        }

    } /* End TabContentRegion */

    /* ************************************************************************
     *
     * TabsMenuManager: manages popup menu with tabs.
     *
     **************************************************************************/
    class TabsMenuManager {

        private ContextMenu popup;

        public TabsMenuManager() {
            TabPane tabPane = getSkinnable();
            setupPopupMenu();
            tabPane.getTabs().addListener(weakTabsListenerForPopup);
            //getProperties().put(ContextMenu.class, popup);
        }

        ListChangeListener<Tab> tabsListenerForPopup = e -> setupPopupMenu();
        WeakListChangeListener weakTabsListenerForPopup =
                new WeakListChangeListener<>(tabsListenerForPopup);

        void dispose() {
            getSkinnable().getTabs().removeListener(weakTabsListenerForPopup);
        }

        private void setupPopupMenu() {
            if (popup == null) {
                popup = new ContextMenu();
            }
            clearPopupMenu();
            ToggleGroup group = new ToggleGroup();
            ObservableList<RadioMenuItem> menuitems = FXCollections.<RadioMenuItem>observableArrayList();
            for (final Tab tab : getSkinnable().getTabs()) {
                TabMenuItem item = new TabMenuItem(tab);
                item.setToggleGroup(group);
                item.setOnAction(t -> {
                    getSkinnable().getSelectionModel().select(tab);
                    getSkinnable().requestFocus();
                });
                menuitems.add(item);
            }
            popup.getItems().addAll(menuitems);
        }

        private void clearPopupMenu() {
            if (this.popup == null) {
                return;
            }
            for (MenuItem item : popup.getItems()) {
                ((TabMenuItem) item).dispose();
            }
            popup.getItems().clear();
        }

        private void showPopupMenu(Node anchor) {
            for (MenuItem mi: popup.getItems()) {
                TabMenuItem tmi = (TabMenuItem)mi;
                if (selectedTab.equals(tmi.getTab())) {
                    tmi.setSelected(true);
                    break;
                }
            }
            popup.show(anchor, Side.BOTTOM, 0, 0);
        }
    } /* End TabsMenuManager*/

    static class TabMenuItem extends RadioMenuItem {
        Tab tab;

        private InvalidationListener disableListener = new InvalidationListener() {
            @Override public void invalidated(Observable o) {
                setDisable(tab.isDisable());
            }
        };

        private WeakInvalidationListener weakDisableListener =
                new WeakInvalidationListener(disableListener);

        public TabMenuItem(final Tab tab) {
            super(tab.getText(), TabPaneProSkin.clone(tab.getGraphic()));
            this.tab = tab;
            setDisable(tab.isDisable());
            tab.disableProperty().addListener(weakDisableListener);
            textProperty().bind(tab.textProperty());
        }

        public Tab getTab() {
            return tab;
        }

        public void dispose() {
            textProperty().unbind();
            tab.disableProperty().removeListener(weakDisableListener);
            tab = null;
        }
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM: return tabHeaderArea.getTabHeaderSkin(selectedTab);
            case ITEM_COUNT: return tabHeaderArea.headersRegion.getChildren().size();
            case ITEM_AT_INDEX: {
                Integer index = (Integer)parameters[0];
                if (index == null) return null;
                return tabHeaderArea.headersRegion.getChildren().get(index);
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    // --------------------------
    // Tab Reordering
    // --------------------------
    private enum DragState {
        NONE,
        START,
        REORDER
    }
    private EventHandler<MouseEvent> headerDraggedHandler = this::handleHeaderDragged;
    private EventHandler<MouseEvent> headerMousePressedHandler = this::handleHeaderMousePressed;
    private EventHandler<MouseEvent> headerMouseReleasedHandler = this::handleHeaderMouseReleased;

    private int dragTabHeaderStartIndex;
    private int dragTabHeaderIndex;
    private TabHeaderSkin dragTabHeader;
    private TabHeaderSkin dropTabHeader;
    private StackPane headersRegion;
    private DragState dragState;
    private final int MIN_TO_MAX = 1;
    private final int MAX_TO_MIN = -1;
    private int xLayoutDirection;
    private double dragEventPrevLoc;
    private int prevDragDirection = MIN_TO_MAX;
    private final double DRAG_DIST_THRESHOLD = 0.75;

    // Reordering Animation
    private final double ANIM_DURATION = 120;
    private TabHeaderSkin dropAnimHeader;
    private double dropHeaderSourceX;
    private double dropHeaderTransitionX;
    private final Animation dropHeaderAnim = new Transition() {
        {
            setInterpolator(Interpolator.EASE_BOTH);
            setCycleDuration(Duration.millis(ANIM_DURATION));
            setOnFinished(event -> {
                completeHeaderReordering();
            });
        }
        @Override
        protected void interpolate(double frac) {
            dropAnimHeader.setLayoutX(dropHeaderSourceX + dropHeaderTransitionX * frac);
        }
    };
    private double dragHeaderDestX;
    private double dragHeaderSourceX;
    private double dragHeaderTransitionX;
    private final Animation dragHeaderAnim = new Transition() {
        {
            setInterpolator(Interpolator.EASE_OUT);
            setCycleDuration(Duration.millis(ANIM_DURATION));
            setOnFinished(event -> {
                reorderTabs();
                resetDrag();
            });
        }
        @Override
        protected void interpolate(double frac) {
            dragTabHeader.setLayoutX(dragHeaderSourceX + dragHeaderTransitionX * frac);
        }
    };

    // Helper methods for managing the listeners based on TabDragPolicy.
    private void addReorderListeners(Node n) {
        n.addEventHandler(MouseEvent.MOUSE_PRESSED, headerMousePressedHandler);
        n.addEventHandler(MouseEvent.MOUSE_RELEASED, headerMouseReleasedHandler);
        n.addEventHandler(MouseEvent.MOUSE_DRAGGED, headerDraggedHandler);
    }

    private void removeReorderListeners(Node n) {
        n.removeEventHandler(MouseEvent.MOUSE_PRESSED, headerMousePressedHandler);
        n.removeEventHandler(MouseEvent.MOUSE_RELEASED, headerMouseReleasedHandler);
        n.removeEventHandler(MouseEvent.MOUSE_DRAGGED, headerDraggedHandler);
    }

    private ListChangeListener childListener = new ListChangeListener<Node>() {
        @Override
        public void onChanged(Change<? extends Node> change) {
            while (change.next()) {
                if (change.wasAdded()) {
                    for(Node n : change.getAddedSubList()) {
                        addReorderListeners(n);
                    }
                }
                if (change.wasRemoved()) {
                    for(Node n : change.getRemoved()) {
                        removeReorderListeners(n);
                    }
                }
            }
        }
    };

    private void updateListeners() {
        if (getSkinnable().getTabDragPolicy() == TabDragPolicy.FIXED ||
                getSkinnable().getTabDragPolicy() == null) {
            for (Node n : headersRegion.getChildren()) {
                removeReorderListeners(n);
            }
            headersRegion.getChildren().removeListener(childListener);
        } else if (getSkinnable().getTabDragPolicy() == TabDragPolicy.REORDER) {
            for (Node n : headersRegion.getChildren()) {
                addReorderListeners(n);
            }
            headersRegion.getChildren().addListener(childListener);
        }
    }

    private void setupReordering(StackPane headersRegion) {
        dragState = DragState.NONE;
        this.headersRegion = headersRegion;
        updateListeners();
        registerChangeListener(getSkinnable().tabDragPolicyProperty(), inv -> updateListeners());
    }

    private void handleHeaderMousePressed(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            startDrag(event);
        }
    }

    private void handleHeaderMouseReleased(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            stopDrag();
            event.consume();
        }
    }

    private void handleHeaderDragged(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            performDrag(event);
        }
    }

    private double getDragDelta(double curr, double prev) {
        if (getSkinnable().getSide().equals(Side.TOP) ||
                getSkinnable().getSide().equals(Side.RIGHT)) {
            return curr - prev;
        } else {
            return prev - curr;
        }
    }

    private int deriveTabHeaderLayoutXDirection() {
        if (getSkinnable().getSide().equals(Side.TOP) ||
                getSkinnable().getSide().equals(Side.RIGHT)) {
            // TabHeaderSkin are laid out in left to right direction inside headersRegion
            return MIN_TO_MAX;
        }
        // TabHeaderSkin are laid out in right to left direction inside headersRegion
        return MAX_TO_MIN;
    }

    private void performDrag(MouseEvent event) {
        if (dragState == DragState.NONE) {
            return;
        }
        int dragDirection = 0;
        double dragHeaderNewLayoutX;
        Bounds dragHeaderBounds;
        Bounds dropHeaderBounds;
        double draggedDist;
        double mouseCurrentLoc = getHeaderRegionLocalX(event);
        double dragDelta = getDragDelta(mouseCurrentLoc, dragEventPrevLoc);

        if (dragDelta > 0) {
            // Dragging the tab header towards higher indexed tab headers inside headersRegion.
            dragDirection = MIN_TO_MAX;
        } else if (dragDelta < 0) {
            // Dragging the tab header towards lower indexed tab headers inside headersRegion.
            dragDirection = MAX_TO_MIN;
        }
        // Stop dropHeaderAnim if direction of drag is changed
        if (dragDirection != 0 && prevDragDirection != dragDirection) {
            stopAnim(dropHeaderAnim);
            prevDragDirection = dragDirection;
        }

        dragHeaderNewLayoutX = dragTabHeader.getLayoutX() + xLayoutDirection * dragDelta;

        if (dragHeaderNewLayoutX >= 0 &&
                dragHeaderNewLayoutX + dragTabHeader.getWidth() <= headersRegion.getWidth()) {

            dragState = DragState.REORDER;
            dragTabHeader.setLayoutX(dragHeaderNewLayoutX);
            dragHeaderBounds = dragTabHeader.getBoundsInParent();

            if (dragDirection == MIN_TO_MAX) {
                // Dragging the tab header towards higher indexed tab headers
                // Last tab header can not be dragged outside headersRegion.

                // When the mouse is moved too fast, sufficient number of events
                // are not generated. Hence it is required to check all possible
                // headers to be reordered.
                for (int i = dragTabHeaderIndex + 1; i < headersRegion.getChildren().size(); i++) {
                    dropTabHeader = (TabHeaderSkin) headersRegion.getChildren().get(i);

                    // dropTabHeader should not be already reordering.
                    if (dropAnimHeader != dropTabHeader) {
                        dropHeaderBounds = dropTabHeader.getBoundsInParent();

                        if (xLayoutDirection == MIN_TO_MAX) {
                            draggedDist = dragHeaderBounds.getMaxX() - dropHeaderBounds.getMinX();
                        } else {
                            draggedDist = dropHeaderBounds.getMaxX() - dragHeaderBounds.getMinX();
                        }

                        // A tab header is reordered when dragged tab header crosses DRAG_DIST_THRESHOLD% of next tab header's width.
                        if (draggedDist > dropHeaderBounds.getWidth() * DRAG_DIST_THRESHOLD) {
                            stopAnim(dropHeaderAnim);
                            // Distance by which tab header should be animated.
                            dropHeaderTransitionX = xLayoutDirection * -dragHeaderBounds.getWidth();
                            if (xLayoutDirection == MIN_TO_MAX) {
                                dragHeaderDestX = dropHeaderBounds.getMaxX() - dragHeaderBounds.getWidth();
                            } else {
                                dragHeaderDestX = dropHeaderBounds.getMinX();
                            }
                            startHeaderReorderingAnim();
                        } else {
                            break;
                        }
                    }
                }
            } else {
                // dragDirection is MAX_TO_MIN
                // Dragging the tab header towards lower indexed tab headers.
                // First tab header can not be dragged outside headersRegion.

                // When the mouse is moved too fast, sufficient number of events
                // are not generated. Hence it is required to check all possible
                // tab headers to be reordered.
                for (int i = dragTabHeaderIndex - 1; i >= 0; i--) {
                    dropTabHeader = (TabHeaderSkin) headersRegion.getChildren().get(i);

                    // dropTabHeader should not be already reordering.
                    if (dropAnimHeader != dropTabHeader) {
                        dropHeaderBounds = dropTabHeader.getBoundsInParent();

                        if (xLayoutDirection == MIN_TO_MAX) {
                            draggedDist = dropHeaderBounds.getMaxX() - dragHeaderBounds.getMinX();
                        } else {
                            draggedDist = dragHeaderBounds.getMaxX() - dropHeaderBounds.getMinX();
                        }

                        // A tab header is reordered when dragged tab crosses DRAG_DIST_THRESHOLD% of next tab header's width.
                        if (draggedDist > dropHeaderBounds.getWidth() * DRAG_DIST_THRESHOLD) {
                            stopAnim(dropHeaderAnim);
                            // Distance by which tab header should be animated.
                            dropHeaderTransitionX = xLayoutDirection * dragHeaderBounds.getWidth();
                            if (xLayoutDirection == MIN_TO_MAX) {
                                dragHeaderDestX = dropHeaderBounds.getMinX();
                            } else {
                                dragHeaderDestX = dropHeaderBounds.getMaxX() - dragHeaderBounds.getWidth();
                            }
                            startHeaderReorderingAnim();
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        dragEventPrevLoc = mouseCurrentLoc;
        event.consume();
    }

    private void startDrag(MouseEvent event) {
        // Stop the animations if any are running from previous reorder.
        stopAnim(dropHeaderAnim);
        stopAnim(dragHeaderAnim);

        dragTabHeader = (TabHeaderSkin) event.getSource();
        if (dragTabHeader != null) {
            dragState = DragState.START;
            xLayoutDirection = deriveTabHeaderLayoutXDirection();
            dragEventPrevLoc = getHeaderRegionLocalX(event);
            dragTabHeaderIndex = headersRegion.getChildren().indexOf(dragTabHeader);
            dragTabHeaderStartIndex = dragTabHeaderIndex;
            dragTabHeader.setViewOrder(0);
            dragHeaderDestX = dragTabHeader.getLayoutX();
        }
    }

    private double getHeaderRegionLocalX(MouseEvent ev) {
        // The event is converted to tab header's parent i.e. headersRegion's local space.
        // This will provide a value of X co-ordinate with all transformations of TabPane
        // and transformations of all nodes in the TabPane's parent hierarchy.
        Point2D sceneToLocalHR = headersRegion.sceneToLocal(ev.getSceneX(), ev.getSceneY());
        return sceneToLocalHR.getX();
    }

    private void stopDrag() {
        if (dragState == DragState.START) {
            // No drag action was performed.
            resetDrag();
        } else if (dragState == DragState.REORDER) {
            // Animate tab header being dragged to its final position.
            dragHeaderSourceX = dragTabHeader.getLayoutX();
            dragHeaderTransitionX = dragHeaderDestX - dragHeaderSourceX;
            dragHeaderAnim.playFromStart();
        }
        tabHeaderArea.invalidateScrollOffset();
    }

    private void reorderTabs() {
        if (dragTabHeaderIndex != dragTabHeaderStartIndex) {
            ((TabObservableList<Tab>) getSkinnable().getTabs()).reorder(
                    getSkinnable().getTabs().get(dragTabHeaderStartIndex),
                    getSkinnable().getTabs().get(dragTabHeaderIndex));
        }
    }

    private void resetDrag() {
        dragState = DragState.NONE;
        dragTabHeader.setViewOrder(1);
        dragTabHeader = null;
        dropTabHeader = null;
        headersRegion.requestLayout();
    }

    // Animate tab header being dropped-on to its new position.
    private void startHeaderReorderingAnim() {
        dropAnimHeader = dropTabHeader;
        dropHeaderSourceX = dropAnimHeader.getLayoutX();
        dropHeaderAnim.playFromStart();
    }

    // Remove dropAnimHeader and add at the index position of dragTabHeader.
    private void completeHeaderReordering() {
        if (dropAnimHeader != null) {
            headersRegion.getChildren().remove(dropAnimHeader);
            headersRegion.getChildren().add(dragTabHeaderIndex, dropAnimHeader);
            dropAnimHeader = null;
            headersRegion.requestLayout();
            dragTabHeaderIndex = headersRegion.getChildren().indexOf(dragTabHeader);
        }
    }

    // Helper method to stop an animation.
    private void stopAnim(Animation anim) {
        if (anim.getStatus() == Animation.Status.RUNNING) {
            anim.getOnFinished().handle(null);
            anim.stop();
        }
    }

}
