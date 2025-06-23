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
import com.techsenger.tabpanepro.core.DragAndDropContext;
import com.techsenger.tabpanepro.core.TabPanePro;
import com.techsenger.tabpanepro.core.skin.TabPaneProSkin;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
import org.kordamp.ikonli.javafx.FontIcon;
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

    private final DragAndDropContext context = new DragAndDropContext();

    private TabPane topPane;

    private TabPane bottomPane;

    private TabPane rightPane;

    private TabPane leftPane;

    private final SplitPane leftSplitPane = new SplitPane();

    private final SplitPane rightSplitPane = new SplitPane();

    private final HBox tabPaneBox = new HBox(leftSplitPane, rightSplitPane);

    private final CheckBox proCheckBox = new CheckBox("Pro");

    private final CheckBox atlantaFxCheckBox = new CheckBox("AtlantaFX");

    private final ComboBox<Integer> tabCountComboBox =
            new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 10, 15, 20, 25));

    private final ComboBox<TabStyle> tabStylesComboBox
            = new ComboBox<>(FXCollections.observableArrayList(TabStyle.values()));

    private final CheckBox headerFirstAreaCheckBox = new CheckBox("Header First Area");

    private final CheckBox headerStickyAreaCheckBox = new CheckBox("Header Sticky Area");

    private final CheckBox headerLastAreaCheckBox = new CheckBox("Header Last Area");

    private final CheckBox headerHiddenWhenEmptyCheckBox = new CheckBox("Header Visible When Empty");

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
            headerFirstAreaCheckBox.setDisable(!newV);
            headerStickyAreaCheckBox.setDisable(!newV);
            headerLastAreaCheckBox.setDisable(!newV);
            headerHiddenWhenEmptyCheckBox.setDisable(!newV);
        });
        atlantaFxCheckBox.setSelected(true);
        atlantaFxCheckBox.selectedProperty().addListener((ov, oldV, newV) -> updateUserAgentStylesheet());
        gridPane.add(atlantaFxCheckBox, 1, 0);
        var tabStylesHBox = new HBox(new Label("Tab Style"), tabStylesComboBox);
        tabStylesHBox.setSpacing(INSET);
        tabStylesHBox.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(tabStylesHBox, 2, 0);
        tabStylesComboBox.getSelectionModel().select(0);
        tabStylesComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabPanes());
        tabStylesComboBox.getStyleClass().add(Styles.DENSE);
        var tabCountHBox = new HBox(new Label("Tab Count"), tabCountComboBox);
        tabCountHBox.setSpacing(INSET);
        tabCountHBox.setAlignment(Pos.CENTER_LEFT);
        tabCountComboBox.getStyleClass().add(Styles.DENSE);
        tabCountComboBox.getSelectionModel().select(4);
        tabCountComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabs());
        gridPane.add(tabCountHBox, 3, 0);

        // row 1
        headerFirstAreaCheckBox.setSelected(true);
        headerFirstAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane.add(headerFirstAreaCheckBox, 0, 1);
        headerStickyAreaCheckBox.setSelected(true);
        headerStickyAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane.add(headerStickyAreaCheckBox, 1, 1);
        headerLastAreaCheckBox.setSelected(true);
        headerLastAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane.add(headerLastAreaCheckBox, 2, 1);
        gridPane.add(headerHiddenWhenEmptyCheckBox,3, 1);
        headerHiddenWhenEmptyCheckBox.setSelected(true);
        headerHiddenWhenEmptyCheckBox.selectedProperty().addListener((ov, oldV, newV) -> updateHeaderVisibleWhenEmpty());

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

    private void updateUserAgentStylesheet() {
        var modenaCss = Demo.class.getResource("modena.css").toExternalForm();
        var atlantaCss = Demo.class.getResource("atlantafx.css").toExternalForm();
        if (atlantaFxCheckBox.isSelected()) {
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            this.root.getScene().getStylesheets().remove(modenaCss);
            this.root.getScene().getStylesheets().add(atlantaCss);
        } else {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            this.root.getScene().getStylesheets().remove(atlantaCss);
            this.root.getScene().getStylesheets().add(modenaCss);
        }
    }

    private void updateHeaderVisibleWhenEmpty() {
        var value = this.headerHiddenWhenEmptyCheckBox.isSelected();
        ((TabPanePro) this.topPane).setHeaderVisibleWhenEmpty(value);
        ((TabPanePro) this.rightPane).setHeaderVisibleWhenEmpty(value);
        ((TabPanePro) this.bottomPane).setHeaderVisibleWhenEmpty(value);
        ((TabPanePro) this.leftPane).setHeaderVisibleWhenEmpty(value);
    }

    private void createTabPanes() {
        this.leftSplitPane.getItems().clear();
        this.rightSplitPane.getItems().clear();
        this.topPane = createTabPane(Side.TOP);
        this.bottomPane = createTabPane(Side.BOTTOM);
        this.leftSplitPane.getItems().addAll(topPane, bottomPane);
        this.rightPane = createTabPane(Side.RIGHT);
        this.leftPane = createTabPane(Side.LEFT);
        this.rightSplitPane.getItems().addAll(leftPane, rightPane);
    }

    private TabPane createTabPane(Side side) {
        final TabPane tabPane;
        if (proCheckBox.isSelected()) {
            TabPanePro tabPanePro = new TabPanePro(context);
            tabPane = tabPanePro;
            tabPanePro.setHeaderVisibleWhenEmpty(this.headerHiddenWhenEmptyCheckBox.isSelected());
            var skin = (TabPaneProSkin) tabPane.getSkin();
            if (headerFirstAreaCheckBox.isSelected()) {
                var button = new Button(null, new FontIcon(MaterialDesignD.DOTS_VERTICAL));
                button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                var container = new StackPane(button);
                container.getStyleClass().add("container");
                container.setMaxHeight(Region.USE_PREF_SIZE);
                skin.getHeaderFirstArea().getChildren().add(container);
            }
            if (headerStickyAreaCheckBox.isSelected()) {
                var button = new Button(null, new FontIcon(MaterialDesignP.PLUS));
                button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                button.setOnAction(e -> {
                    addTabTo(tabPane);
                });
                var container = new StackPane(button);
                container.getStyleClass().add("container");
                container.setMaxHeight(Region.USE_PREF_SIZE);
                skin.getHeaderStickyArea().getChildren().add(container);
            }
            if (headerLastAreaCheckBox.isSelected()) {
                var tabsMenuButton = new Button(null, new FontIcon(MaterialDesignM.MENU_DOWN));
                tabsMenuButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                tabsMenuButton.visibleProperty().bind(skin.tabsMenuRequiredProperty());
                tabsMenuButton.setOnAction(e -> skin.showTabsMenu(tabsMenuButton));

                var minimizeButton = new Button(null, new FontIcon(MaterialDesignM.MINUS));
                minimizeButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                var container = new HBox(tabsMenuButton, minimizeButton);
                container.setMaxHeight(Region.USE_PREF_SIZE);
                container.getStyleClass().add("container");
                skin.getHeaderLastArea().getChildren().add(container);
            }
        } else {
            tabPane = new TabPane();
        }

        if (tabStylesComboBox.getValue() == TabStyle.CLASSIC) {
            tabPane.getStyleClass().add(Styles.TABS_CLASSIC);
        } else if (tabStylesComboBox.getValue() == TabStyle.FLOATING) {
            tabPane.getStyleClass().add(Styles.TABS_FLOATING);
        }

        tabPane.setSide(side);
        tabPane.setMaxWidth(Double.MAX_VALUE);
        createTabs(tabPane);
        return tabPane;
    }

    private void createTabs() {
        createTabs(topPane);
        createTabs(rightPane);
        createTabs(bottomPane);
        createTabs(leftPane);
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
        //content.setStyle("-fx-background-color: -color-bg-subtle");
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
