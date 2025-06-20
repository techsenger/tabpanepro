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

import atlantafx.base.controls.Spacer;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignH;

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

    private final ComboBox<Integer> tabCountComboBox =
            new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 10, 15, 20, 25));

    private final ComboBox<TabStyle> tabStylesComboBox
            = new ComboBox<>(FXCollections.observableArrayList(TabStyle.values()));

    private final CheckBox headerFirstAreaCheckBox = new CheckBox("Header First Area");

    private final CheckBox headerLastAreaCheckBox = new CheckBox("Header Last Area");

    private final GridPane controlGridPane = new GridPane();

    private final StackPane controlPane = new StackPane(controlGridPane);

    private final VBox root = new VBox(tabPaneBox, controlPane);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        HBox.setHgrow(leftSplitPane, Priority.ALWAYS);
        leftSplitPane.setOrientation(Orientation.VERTICAL);
        HBox.setHgrow(rightSplitPane, Priority.ALWAYS);
        tabPaneBox.setSpacing(INSET);
        VBox.setVgrow(tabPaneBox, Priority.ALWAYS);

        proCheckBox.setSelected(true);
        proCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        tabStylesComboBox.getSelectionModel().select(0);
        tabStylesComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabPanes());
        tabCountComboBox.getSelectionModel().select(3);
        tabCountComboBox.valueProperty().addListener((ov, oldV, newV) -> createTabs());

        controlGridPane.setVgap(INSET);
        controlGridPane.setHgap(INSET);
        controlGridPane.add(proCheckBox, 0, 0, 2, 1);
        controlGridPane.add(new Spacer(3 * INSET, Orientation.HORIZONTAL), 2, 0);
        controlGridPane.add(new Label("Tab Style"), 3, 0);
        controlGridPane.add(tabStylesComboBox, 4, 0);
        controlGridPane.add(new Spacer(3 * INSET, Orientation.HORIZONTAL), 5, 0);
        controlGridPane.add(new Label("Tab Count"), 6, 0);
        controlGridPane.add(tabCountComboBox, 7, 0);

        headerFirstAreaCheckBox.setSelected(true);
        headerFirstAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());
        headerLastAreaCheckBox.setSelected(true);
        headerLastAreaCheckBox.selectedProperty().addListener((ov, oldV, newV) -> createTabPanes());

        controlGridPane.add(headerFirstAreaCheckBox, 0, 1, 2, 1);
        controlGridPane.add(headerLastAreaCheckBox, 3, 1, 2, 1);

        createTabPanes();

        root.setSpacing(2 * INSET);
        root.setPadding(new Insets(INSET));
        Scene scene = new Scene(root, 1280, 600);
        scene.getStylesheets().add(Demo.class.getResource("demo.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("TabPanePro Demo");
        primaryStage.show();
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
            if (headerFirstAreaCheckBox.isSelected()) {
                var button = new Button(null, new FontIcon(MaterialDesignH.HELP));
                button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
                button.setShape(new Circle(20));
                var container = new StackPane(button);
                container.getStyleClass().add("container");
                ((TabPaneProSkin) tabPane.getSkin()).getHeaderFirstArea().getChildren().add(container);
            }
        } else {
            tabPane = new TabPane();
        }

        if (tabStylesComboBox.getValue() == TabStyle.CLASSIC) {
            tabPane.getStyleClass().add(Styles.TABS_CLASSIC);
        } else if (tabStylesComboBox.getValue() == TabStyle.FLOATING) {
            tabPane.getStyleClass().add(Styles.TABS_FLOATING);
        }

        HBox.setHgrow(tabPane, Priority.ALWAYS);
        tabPane.setSide(side);
        tabPane.setMaxWidth(Double.MAX_VALUE);
        //tabPane.getStyleClass().add(Styles.DENSE);
        // forces equal width in HBox layout regardless of tab count
        tabPane.setPrefWidth(0);
        HBox.setHgrow(tabPane, Priority.ALWAYS);
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
            String side = tabPane.getSide().name();
            var t = new Tab(side + " " + i);
            tabPane.getTabs().add(t);
            var content = new VBox();
            content.setSpacing(INSET);
            //content.setStyle("-fx-background-color: -color-bg-subtle");
            content.setPadding(new Insets(INSET));
            for (var z = 0; z < 2; z++) {
                content.getChildren().add(new Label("Label " + i + " / " + z));
                var textArea = new TextField("TextField " + i + " / " + z);
                content.getChildren().add(textArea);
            }
            t.setContent(content);
        }
    }
}
