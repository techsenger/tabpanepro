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

import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.Styles;
import com.techsenger.tabpanepro.core.DragAndDropContext;
import com.techsenger.tabpanepro.core.TabPanePro;
import com.techsenger.tabpanepro.core.skin.TabPaneProSkin;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Pavel Castornii
 */
public class Demo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int INSET = 10;

    private final DragAndDropContext context = new DragAndDropContext();

    private final ComboBox<Integer> tabCountComboBox =
            new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 10, 15, 20, 25));

    private final GridPane gridPane = new GridPane();

    private final StackPane centerPane = new StackPane(gridPane);

    private final TabPanePro topPane = createTabPane(Side.TOP);

    private final TabPanePro rightPane = createTabPane(Side.RIGHT);

    private final TabPanePro bottomPane = createTabPane(Side.BOTTOM);

    private final TabPanePro leftPane = createTabPane(Side.LEFT);

    private final BorderPane borderPane = new BorderPane(centerPane, topPane, rightPane, bottomPane, leftPane);

//    private final BorderPane borderPane = new BorderPane(centerPane, topPane, null, null, null);

//    private final BorderPane borderPane = new BorderPane(centerPane, null, rightPane, null, null);
//
//    private final BorderPane borderPane = new BorderPane(centerPane, null, null, bottomPane, null);
//
//    private final BorderPane borderPane = new BorderPane(centerPane, null, null, null, leftPane);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        gridPane.add(new Label("Tab Count"), 0, 0);
        tabCountComboBox.valueProperty().addListener((ov, oldV, newV) -> {
            createTabs(topPane);
            createTabs(rightPane);
            createTabs(bottomPane);
            createTabs(leftPane);
        });
        gridPane.add(tabCountComboBox, 1, 0);
        gridPane.setVgap(INSET);
        gridPane.setHgap(INSET);
        gridPane.setPadding(new Insets(INSET));
        gridPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        rightPane.setPrefWidth(200);
        leftPane.setPrefWidth(200);

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(Demo.class.getResource("demo.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("TabPanePro Demo");
        primaryStage.show();
    }

    private TabPanePro createTabPane(Side side) {
        var tabPane = new TabPanePro(context);
        tabPane.setSkin(new TabPaneProSkin(tabPane));
        tabPane.setSide(side);
        tabPane.setMaxWidth(Double.MAX_VALUE);
        tabPane.getStyleClass().add(Styles.DENSE);
        // forces equal width in HBox layout regardless of tab count
        tabPane.setPrefWidth(0);
        HBox.setHgrow(tabPane, Priority.ALWAYS);
        return tabPane;
    }

    private TabPanePro createTabs(TabPanePro tabPane) {
        tabPane.getTabs().clear();
        for (var i = 0; i < this.tabCountComboBox.getValue(); i++) {
            String side = tabPane.getSide().name();
            var t = new Tab(side + " " + i);
            tabPane.getTabs().add(t);
            var content = new VBox();
            content.setSpacing(10);
            //content.setStyle("-fx-background-color: -color-bg-subtle");
            content.setPadding(new Insets(10));
            for (var z = 0; z < 2; z++) {
                content.getChildren().add(new Label("Label " + i + " / " + z));
                var textArea = new TextField("TextField " + i + " / " + z);
                content.getChildren().add(textArea);
            }
            t.setContent(content);
        }
        return tabPane;
    }
}
