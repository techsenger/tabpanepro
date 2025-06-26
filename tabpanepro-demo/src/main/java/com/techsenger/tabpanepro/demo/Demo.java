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

package com.techsenger.tabpanepro.demo;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.Styles;
import com.techsenger.tabpanepro.core.TabPanePro;
import com.techsenger.tabpanepro.core.skin.TabHeaderAreaPolicy;
import com.techsenger.tabpanepro.core.skin.TabPaneProSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignM;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;

/**
 *
 * @author Pavel Castornii
 */
public class Demo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int INSET = 10;

    private static final String SHORT_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    private static final String LONG_TEXT =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin id augue id nibh vestibulum "
                + "imperdiet et quis sem. Quisque finibus, ante a tempor consectetur, diam purus tempor nunc, "
                + "tempor auctor orci libero vitae quam.";

    private enum TabStyle {
        BASE, FLOATING, CLASSIC
    }

    private enum TabScrollBarStyle {
        ABOVE_TABS, ABOVE_TABS_AND_STICKY, BELOW_TABS, BELOW_TABS_AND_STICKY
    }

    private enum CssTest {
        NO_TEST, AREA_POSITION, HEADER_PADDING, AREA_MIN_HEIGHT, AREA_MIN_HEIGHT_AND_HEADER_PADDING
    }

    private final List<? extends TabPane> tabPanes = new ArrayList<>();

    private final SplitPane leftSplitPane = new SplitPane();

    private final SplitPane rightSplitPane = new SplitPane();

    private final HBox tabPaneBox = new HBox(leftSplitPane, rightSplitPane);

    private final CheckBox proCheckBox = new CheckBox("Pro");

    private final CheckBox cupertinoDarkCheckBox = new CheckBox("Cupertino Dark");

    private final ComboBox<Integer> tabCountComboBox =
            new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 10, 15, 20, 25));

    private final ComboBox<TabStyle> tabStyleComboBox
            = new ComboBox<>(FXCollections.observableArrayList(TabStyle.values()));

    private final CheckBox firstAreaCheckBox = new CheckBox("First Area");

    private final CheckBox stickyAreaCheckBox = new CheckBox("Sticky Area");

    private final CheckBox lastAreaCheckBox = new CheckBox("Last Area");

    private final Label tabHeaderAreaPolicyLabel = new Label("Tab Header Area Policy");

    private final ComboBox<TabHeaderAreaPolicy> tabHeaderAreaPolicyComboBox
            = new ComboBox<>(FXCollections.observableArrayList(TabHeaderAreaPolicy.values()));

    private final CheckBox scrollBarCheckBox = new CheckBox("ScrollBar");

    private final Label scrollBarStyleLabel = new Label("ScrollBar Style");

    private final ComboBox<TabScrollBarStyle> tabScrollBarStyleComboBox
            = new ComboBox<>(FXCollections.observableArrayList(TabScrollBarStyle.values()));

    private final Label cssTestLabel = new Label("CSS Test");

    private final ComboBox<CssTest> cssTestComboBox
            = new ComboBox<>(FXCollections.observableArrayList(CssTest.values()));

    private final GridPane gridPane = new GridPane();

    private final VBox root = new VBox(tabPaneBox, gridPane);

    @Override
    public void start(Stage primaryStage) throws Exception {
        // forces equal width in HBox layout regardless of tab count
        leftSplitPane.setPrefWidth(0);
        HBox.setHgrow(leftSplitPane, Priority.ALWAYS);
        leftSplitPane.setOrientation(Orientation.VERTICAL);
        HBox.setHgrow(rightSplitPane, Priority.ALWAYS);
        rightSplitPane.setPrefWidth(0);
        tabPaneBox.setSpacing(INSET);
        VBox.setVgrow(tabPaneBox, Priority.ALWAYS);

        for (var i = 0; i < 4; i++) {
            ColumnConstraints con = new ColumnConstraints();
            con.setPercentWidth(25);
            gridPane.getColumnConstraints().add(con);
        }
        gridPane.setHgap(INSET);
        gridPane.setVgap(INSET);

        // row 0
        gridPane.add(proCheckBox, 0, 0);
        proCheckBox.setSelected(true);
        proCheckBox.selectedProperty().addListener((ov, oldV, newV) -> {
            createTabPanes();
            firstAreaCheckBox.setDisable(!newV);
            stickyAreaCheckBox.setDisable(!newV);
            lastAreaCheckBox.setDisable(!newV);
            tabHeaderAreaPolicyLabel.setDisable(!newV);
            tabHeaderAreaPolicyComboBox.setDisable(!newV);
            cssTestLabel.setDisable(!newV);
            cssTestComboBox.setDisable(!newV);
            scrollBarCheckBox.setDisable(!newV);
            scrollBarStyleLabel.setDisable(!newV);
            tabScrollBarStyleComboBox.setDisable(!newV);
        });
        cupertinoDarkCheckBox.setSelected(true);
        cupertinoDarkCheckBox.selectedProperty().addListener((ov, oldV, newV) -> updateUserAgentStylesheet());
        gridPane.add(cupertinoDarkCheckBox, 1, 0);
        var tabStylesHBox = createCellHBox(new Label("Tab Style"), tabStyleComboBox);
        gridPane.add(tabStylesHBox, 2, 0);
        tabStyleComboBox.getSelectionModel().select(0);
        tabStyleComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabPanes());
        tabStyleComboBox.getStyleClass().add(Styles.DENSE);
        tabStyleComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabStyleComboBox, Priority.ALWAYS);
        var tabCountHBox = createCellHBox(new Label("Tab Count"), tabCountComboBox);
        tabCountComboBox.getStyleClass().add(Styles.DENSE);
        tabCountComboBox.getSelectionModel().select(4);
        tabCountComboBox.valueProperty().addListener((ov, oldV, newV) -> tabPanes.forEach(e -> createTabs(e)));
        tabCountComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabCountComboBox, Priority.ALWAYS);
        gridPane.add(tabCountHBox, 3, 0);

        // row 1
        firstAreaCheckBox.setSelected(true);
        firstAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane.add(firstAreaCheckBox, 0, 1);
        stickyAreaCheckBox.setSelected(true);
        stickyAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane.add(stickyAreaCheckBox, 1, 1);
        lastAreaCheckBox.setSelected(true);
        lastAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane.add(lastAreaCheckBox, 2, 1);
        tabHeaderAreaPolicyLabel.setMinWidth(Region.USE_PREF_SIZE);
        var policyHBox = createCellHBox(tabHeaderAreaPolicyLabel, tabHeaderAreaPolicyComboBox);
        gridPane.add(policyHBox,3, 1);
        tabHeaderAreaPolicyComboBox.getSelectionModel().select(1);
        tabHeaderAreaPolicyComboBox.valueProperty().addListener((ov, oldV, newV) -> updatePolicy(newV));
        tabHeaderAreaPolicyComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabHeaderAreaPolicyComboBox, Priority.ALWAYS);

        //row 2
        gridPane.add(scrollBarCheckBox, 0, 2);
        scrollBarCheckBox.selectedProperty().addListener((ov, oldV, newV) -> updateScrollBarEnabled(newV));
        scrollBarStyleLabel.setMinWidth(Region.USE_PREF_SIZE);
        tabScrollBarStyleComboBox.getSelectionModel().select(2);
        tabScrollBarStyleComboBox.valueProperty()
                .addListener((ov, oldV, newV) -> updateTabScrollBarStyle(oldV, newV));
        tabScrollBarStyleComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabScrollBarStyleComboBox, Priority.ALWAYS);
        var scrollBarStyleHBox = createCellHBox(scrollBarStyleLabel, tabScrollBarStyleComboBox);
        gridPane.add(scrollBarStyleHBox, 1, 2);

        cssTestLabel.setMinWidth(Region.USE_PREF_SIZE);
        var testsHBox = createCellHBox(cssTestLabel, cssTestComboBox);
        cssTestComboBox.getSelectionModel().select(0);
        cssTestComboBox.valueProperty().addListener((ov, oldV, newV) -> updateCssTest(oldV, newV));
        cssTestComboBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cssTestComboBox, Priority.ALWAYS);
        GridPane.setHgrow(testsHBox, Priority.ALWAYS);
        gridPane.add(testsHBox, 2, 2, 2, 1);

        root.setSpacing(2 * INSET);
        root.setPadding(new Insets(INSET));
        Scene scene = new Scene(root, 1280, 720);
        updateUserAgentStylesheet();
        createTabPanes();
        scene.getStylesheets().add(Demo.class.getResource("demo.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("TabPanePro Demo");
        primaryStage.show();
    }

    private HBox createCellHBox(Node ...nodes) {
        var box = new HBox(nodes);
        box.setSpacing(INSET);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void updateUserAgentStylesheet() {
        var modenaCss = Demo.class.getResource("modena.css").toExternalForm();
        var cupertinoDarkCss = Demo.class.getResource("cupertino-dark.css").toExternalForm();
        if (cupertinoDarkCheckBox.isSelected()) {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            this.root.getScene().getStylesheets().remove(modenaCss);
            this.root.getScene().getStylesheets().add(cupertinoDarkCss);
        } else {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            this.root.getScene().getStylesheets().remove(cupertinoDarkCss);
            this.root.getScene().getStylesheets().add(modenaCss);
        }
    }

    private void updatePolicy(TabHeaderAreaPolicy policy) {
        tabPanes.stream().map(e -> (TabPaneProSkin) e.getSkin()).forEach(e -> e.setTabHeaderAreaPolicy(policy));
    }

    private void updateScrollBarEnabled(boolean value) {
        tabPanes.stream().map(e -> (TabPaneProSkin) e.getSkin()).forEach(e -> e.setTabScrollBarEnabled(value));
    }

    private void updateTabScrollBarStyle(TabScrollBarStyle oldStyle, TabScrollBarStyle newStyle) {
        if (oldStyle != null) {
            var styleClass = resolveTabScrollBarStyleClass(oldStyle);
            tabPanes.stream().forEach(e -> e.getStyleClass().remove(styleClass));
        }
        if (newStyle != null) {
            var styleClass = resolveTabScrollBarStyleClass(newStyle);
            tabPanes.stream().forEach(e -> e.getStyleClass().add(styleClass));
        }
    }

    private String resolveTabScrollBarStyleClass(TabScrollBarStyle style) {
        return "tsb-" + style.name().toLowerCase().replace("_", "-");
    }

    private void updateCssTest(CssTest oldValue, CssTest newValue) {
        if (oldValue != CssTest.NO_TEST) {
            tabPanes.stream().forEach(e -> {
                e.getStyleClass().removeAll("test", resolveTestStyleClass(oldValue));
            });
        }
        if (newValue != CssTest.NO_TEST) {
            tabPanes.stream().forEach(e -> {
                e.getStyleClass().addAll("test", resolveTestStyleClass(newValue));
            });
        }
        tabPanes.stream().forEach(e -> e.requestLayout());
    }

    private String resolveTestStyleClass(CssTest test) {
        return test.name().toLowerCase().replace("_", "-") + "-test";
    }

    private void createTabPanes() {
        this.leftSplitPane.getItems().clear();
        this.rightSplitPane.getItems().clear();
        var topPane = createTabPane(Side.TOP);
        var bottomPane = createTabPane(Side.BOTTOM);
        this.leftSplitPane.getItems().addAll(topPane, bottomPane);
        var rightPane = createTabPane(Side.RIGHT);
        var leftPane = createTabPane(Side.LEFT);
        this.rightSplitPane.getItems().addAll(leftPane, rightPane);
        this.tabPanes.clear();
        this.tabPanes.addAll((List) List.of(topPane, rightPane, bottomPane, leftPane));
        updateCssTest(CssTest.NO_TEST, cssTestComboBox.getValue());
        updateTabScrollBarStyle(null, tabScrollBarStyleComboBox.getValue());
    }

    private TabPane createTabPane(Side side) {
        final TabPane tabPane;
        if (proCheckBox.isSelected()) {
            TabPanePro tabPanePro = new TabPanePro();
            tabPane = tabPanePro;
            var skin = (TabPaneProSkin) tabPane.getSkin();
            if (firstAreaCheckBox.isSelected()) {
                skin.getTabHeaderFirstArea().getChildren().add(createFirstAreaContainer());
            }
            if (stickyAreaCheckBox.isSelected()) {

                skin.getTabHeaderStickyArea().getChildren().add(createStickyAreaContainer(tabPane));
            }
            if (lastAreaCheckBox.isSelected()) {
                skin.getTabHeaderLastArea().getChildren().add(createLastAreaContainer(skin));
            }
            skin.setTabHeaderAreaPolicy(this.tabHeaderAreaPolicyComboBox.getValue());
            skin.setTabScrollBarEnabled(scrollBarCheckBox.isSelected());
        } else {
            tabPane = new TabPane();
        }

        if (tabStyleComboBox.getValue() == TabStyle.CLASSIC) {
            tabPane.getStyleClass().add(Styles.TABS_CLASSIC);
        } else if (tabStyleComboBox.getValue() == TabStyle.FLOATING) {
            tabPane.getStyleClass().add(Styles.TABS_FLOATING);
        }

        tabPane.setSide(side);
        tabPane.setMaxWidth(Double.MAX_VALUE);
        createTabs(tabPane);
        return tabPane;
    }

    private Node createFirstAreaContainer() {
        var button = new Button(null, new FontIcon(MaterialDesignD.DOTS_VERTICAL));
        button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        var container = new StackPane(button);
        container.getStyleClass().add("container");
        container.setMaxHeight(Region.USE_PREF_SIZE);
        return container;
    }

    private Node createStickyAreaContainer(TabPane tabPane) {
        var button = new Button(null, new FontIcon(MaterialDesignP.PLUS));
        button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        button.setOnAction(e -> {
            addTabTo(tabPane);
            tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
        });
        var container = new StackPane(button);
        container.getStyleClass().add("container");
        container.setMaxHeight(Region.USE_PREF_SIZE);
        return container;
    }

    private Node createLastAreaContainer(TabPaneProSkin skin) {
        var tabsMenuButton = new Button(null, new FontIcon(MaterialDesignM.MENU_DOWN));
        tabsMenuButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        tabsMenuButton.visibleProperty().bind(skin.headersRegionOverflowedProperty());
        tabsMenuButton.setOnAction(e -> skin.showTabsMenu(tabsMenuButton));

        var leftTimeline = new Timeline(new KeyFrame(Duration.millis(25), e -> skin.scrollTabHeadersBy(10)));
        leftTimeline.setCycleCount(Timeline.INDEFINITE);
        var scrollLeftButton = new Button(null, new FontIcon(MaterialDesignC.CHEVRON_LEFT));
        var leftEdge = Bindings.createBooleanBinding(
                () -> skin.getHeadersRegionOffset() >= 0 - 0.000001,
                skin.headersRegionOffsetProperty());
        scrollLeftButton.disableProperty().bind(leftEdge.or(skin.headersRegionOverflowedProperty().not()));
        scrollLeftButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        scrollLeftButton.setOnMousePressed(e -> leftTimeline.play());
        scrollLeftButton.setOnMouseReleased(e -> leftTimeline.stop());

        var rightTimeline = new Timeline(new KeyFrame(Duration.millis(25), e -> skin.scrollTabHeadersBy(-10)));
        rightTimeline.setCycleCount(Timeline.INDEFINITE);
        var scrollRightButton = new Button(null, new FontIcon(MaterialDesignC.CHEVRON_RIGHT));
        var rightEdge = Bindings.createBooleanBinding(
                () -> {
                    // If snapToPixel is true, then all values (including the offset) are snapped
                    return skin.getHeadersRegionWidth() - 0.000001 <=  skin.getHeadersClipWidth()
                        + Math.abs(skin.getHeadersRegionOffset());
                },
                skin.headersRegionOffsetProperty(),
                skin.headersRegionWidthProperty(),
                skin.headersClipWidthProperty());
        scrollRightButton.disableProperty().bind(rightEdge.or(skin.headersRegionOverflowedProperty().not()));

        scrollRightButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        scrollRightButton.setOnMousePressed(e -> rightTimeline.play());
        scrollRightButton.setOnMouseReleased(e -> rightTimeline.stop());

        var minimizeButton = new Button(null, new FontIcon(MaterialDesignM.MINUS));
        minimizeButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);

        var container = new HBox(tabsMenuButton, scrollLeftButton, scrollRightButton, minimizeButton);
        container.setMaxHeight(Region.USE_PREF_SIZE);
        container.getStyleClass().add("container");
        return container;
    }

    private void createTabs(TabPane tabPane) {
        tabPane.getTabs().clear();
        for (var i = 0; i < this.tabCountComboBox.getValue(); i++) {
            addTabTo(tabPane);
        }
    }

    private void addTabTo(TabPane tabPane) {
        String side = tabPane.getSide().name();
        var index = tabPane.getTabs().size();
        var tab = new Tab(side + " " + index);
        var content = new VBox();
        content.setSpacing(INSET);
        content.setPadding(new Insets(INSET));
        content.getChildren().add(new Label(side + " " + index));
        var textField = new TextField(SHORT_TEXT);
        content.getChildren().add(textField);
        var textArea = new TextArea(LONG_TEXT);
        textArea.setWrapText(true);
        textArea.setPrefHeight(0);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        content.getChildren().add(textArea);
        tab.setContent(content);
        tabPane.getTabs().add(tab);
    }
}
