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

/**
 *
 * @author Pavel Castornii
 */
public class Demo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int INSET = 10;

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

    private final CheckBox headerLastAreaCheckBox = new CheckBox("Header Last Area");

    private final GridPane mainGridPane = new GridPane();

    private final GridPane gridPane0 = new GridPane();

    private final GridPane gridPane1 = new GridPane();

    private final GridPane gridPane2 = new GridPane();

    private final GridPane gridPane3 = new GridPane();

    private final VBox root = new VBox(tabPaneBox, mainGridPane);

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
            mainGridPane.getColumnConstraints().add(con);
        }
        mainGridPane.setHgap(INSET);
        mainGridPane.add(gridPane0, 0, 0);
        mainGridPane.add(gridPane1, 1, 0);
        mainGridPane.add(gridPane2, 2, 0);
        mainGridPane.add(gridPane3, 3, 0);
        mainGridPane.setHgap(INSET);

        gridPane0.setVgap(INSET);
        gridPane0.setHgap(INSET);
        gridPane0.add(proCheckBox, 0, 0, 2, 1);
        proCheckBox.setSelected(true);
        proCheckBox.selectedProperty().addListener((ov, oldV, newV) -> {
            createTabPanes();
            headerFirstAreaCheckBox.setDisable(!newV);
            headerLastAreaCheckBox.setDisable(!newV);
        });
        headerFirstAreaCheckBox.setSelected(true);
        headerFirstAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane0.add(headerFirstAreaCheckBox, 0, 1, 2, 1);

        gridPane1.setVgap(INSET);
        gridPane1.setHgap(INSET);
        atlantaFxCheckBox.setSelected(true);
        atlantaFxCheckBox.selectedProperty().addListener((ov, oldV, newV) -> updateUserAgentStylesheet());
        gridPane1.add(atlantaFxCheckBox, 0, 0, 2, 1);
        headerLastAreaCheckBox.setSelected(true);
        headerLastAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        gridPane1.add(headerLastAreaCheckBox, 0, 1, 2, 1);

        gridPane2.setVgap(INSET);
        gridPane2.setHgap(INSET);
        gridPane2.add(new Label("Tab Style"), 0, 0);
        gridPane2.add(tabStylesComboBox, 1, 0);
        tabStylesComboBox.getSelectionModel().select(2);
        tabStylesComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabPanes());

        gridPane3.setVgap(INSET);
        gridPane3.setHgap(INSET);
        gridPane3.add(new Label("Tab Count"), 0, 0);
        gridPane3.add(tabCountComboBox, 1, 0);
        tabCountComboBox.getSelectionModel().select(4);
        tabCountComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabs());

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
        TabPane tabPane = null;
        if (proCheckBox.isSelected()) {
            tabPane = new TabPanePro(context);
            var skin = (TabPaneProSkin) tabPane.getSkin();
            if (headerFirstAreaCheckBox.isSelected()) {
                var button = new Button(null, new FontIcon(MaterialDesignD.DOTS_VERTICAL));
                button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                var container = new StackPane(button);
                container.getStyleClass().add("container");
                container.setMaxHeight(Region.USE_PREF_SIZE);
                skin.getHeaderFirstArea().getChildren().add(container);
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
        var text1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        var text2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin id augue id nibh vestibulum "
                + "imperdiet et quis sem. Quisque finibus, ante a tempor consectetur, diam purus tempor nunc, "
                + "tempor auctor orci libero vitae quam.";
        for (var i = 0; i < this.tabCountComboBox.getValue(); i++) {
            String side = tabPane.getSide().name();
            var t = new Tab(side + " " + i);
            tabPane.getTabs().add(t);
            var content = new VBox();
            content.setSpacing(INSET);
            //content.setStyle("-fx-background-color: -color-bg-subtle");
            content.setPadding(new Insets(INSET));
            content.getChildren().add(new Label(side + " " + i));
            var textField = new TextField(text1);
            content.getChildren().add(textField);
            var textArea = new TextArea(text2);
            textArea.setWrapText(true);
            textArea.setPrefHeight(0);
            VBox.setVgrow(textArea, Priority.ALWAYS);
            content.getChildren().add(textArea);
            t.setContent(content);
        }
    }
}
