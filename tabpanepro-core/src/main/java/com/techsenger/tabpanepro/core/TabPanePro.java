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

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 *
 * @author Pavel Castornii
 */
public class TabPanePro extends TabPane {

    private static final DataFormat DRAGGABLE_TAB_FORMAT = new DataFormat("application/draggable-tab-format");

    private static double TAB_IMAGE_SIZE = 240;

    private final DragAndDropContext context;

    private final ObjectProperty<StackPane> dropPrevTab = new SimpleObjectProperty<>();

    private final ObjectProperty<StackPane> dropNextTab = new SimpleObjectProperty<>();

    private StackPane headersRegion;

    private StackPane headerArea;

    public TabPanePro(DragAndDropContext context) {
        this(context, null);
    }

    public TabPanePro(DragAndDropContext context, Tab... tabs) {
        super(tabs);
        // setSkin(new TabPaneProSkin(this));
        this.context = context;
        this.getStylesheets().add(TabPanePro.class.getResource("tab-pane-pro.css").toExternalForm());
        this.dropPrevTab.addListener((ov, oldV, newV) -> updateDropTab(oldV, newV, "drop-prev-tab"));
        this.dropNextTab.addListener((ov, oldV, newV) -> updateDropTab(oldV, newV, "drop-next-tab"));
        // the node to the headers region will be added only on the secod pulse after adding tab
        getTabs().addListener((ListChangeListener<? super Tab>) (change) -> Platform.runLater(this::initTabs));
        sceneProperty().addListener((ov, oldV, newV) -> {
            if (newV != null) {
                Platform.runLater(() -> {
                    this.headersRegion = (StackPane) lookup(".headers-region");
                    this.headersRegion.translateXProperty().addListener((ov2, oldV2, newV2) ->
                            System.out.println("HR:" + newV2.doubleValue()));
                    this.headerArea = (StackPane) this.headersRegion.getParent();
                    this.headerArea.translateXProperty().addListener((ov2, oldV2, newV2) ->
                            System.out.println("HA:" + newV2.doubleValue()));
                    this.headerArea.setOnDragOver(e -> doOnDragOver(e));
                    this.headerArea.setOnDragExited(e -> doOnDragExited(e));
                    this.headerArea.setOnDragDropped(e -> doOnDragDropped(e));
                    initTabs();
                });
            }
        });
    }

    private void initTabs() {
        if (this.headersRegion == null) {
            return;
        }
        int index = 0;
        for (var tabNode : headersRegion.getChildren()) {
            tabNode.setUserData(index++);
            if (tabNode.getOnDragDetected() == null) {
                tabNode.setOnDragDetected(e -> doOnDragDetected((StackPane) tabNode, e));
                tabNode.setOnDragOver(e -> doOnDragOver((StackPane) tabNode, e));
            }
        }
    }

    private void doOnDragDetected(StackPane tabNode, MouseEvent e) {
        Dragboard db = tabNode.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.put(DRAGGABLE_TAB_FORMAT, "tab");
        var descriptor = new TabDescriptor(getSelectionModel().getSelectedItem(), tabNode.getWidth());
        this.context.setTabDescriptor(descriptor);
        db.setContent(content);
        var color = Color.web("#ffe6ab"); // todo:
        var image = createImagedTab(tabNode, 1, color);
        db.setDragView(image);
    }

    private void doOnDragOver(StackPane hoveredNode, DragEvent e) {
        final Integer tabIndex = (Integer) hoveredNode.getUserData();
        if (e.getX() <= hoveredNode.getWidth() / 2) {
            this.dropNextTab.set(hoveredNode);
            this.context.setDropIndex(tabIndex);
            if (tabIndex - 1 >= 0) {
                this.dropPrevTab.set((StackPane) this.headersRegion.getChildren().get(tabIndex - 1));
            } else {
                this.dropPrevTab.set(null);
            }
        } else {
            this.dropPrevTab.set(hoveredNode);
            this.context.setDropIndex(tabIndex + 1);
            if (tabIndex + 1 < this.headersRegion.getChildren().size()) {
                this.dropNextTab.set((StackPane) this.headersRegion.getChildren().get(tabIndex + 1));
            } else {
                this.dropNextTab.set(null);
            }
        }
    }

    private void doOnDragOver(DragEvent e) {
        if (!e.getDragboard().hasContent(DRAGGABLE_TAB_FORMAT)) {
            return;
        }
        e.acceptTransferModes(TransferMode.MOVE);
        e.consume();
    }

    private void doOnDragDropped(DragEvent e) {
        if (!e.getDragboard().hasContent(DRAGGABLE_TAB_FORMAT)) {
            return;
        }
        var descriptor = this.context.getTabDescriptor();
        if (descriptor == null) {
            return;
        }
        // don't remove tab by index - as the tab won't be removed
        descriptor.getTab().getTabPane().getTabs().remove(descriptor.getTab());
        getTabs().add(context.getDropIndex(), descriptor.getTab());
        this.context.clear();
        e.setDropCompleted(true);
        e.consume();
    }

    private void doOnDragExited(DragEvent e) {
        this.dropPrevTab.set(null);
        this.dropNextTab.set(null);
    }

    private void updateDropTab(StackPane oldTab, StackPane newTab, String styleClass) {
        if (oldTab != null) {
            oldTab.getStyleClass().remove(styleClass);
        }
        if (newTab != null) {
            newTab.getStyleClass().add(styleClass);
        }
    }

    private Image createImagedTab(StackPane tabNode, int borderWidth, Color borderColor) {
        // 1. Capture snapshots of tab header and content
        var tabImage = createTabImage(tabNode, borderWidth);
        var contentImage = createContentImage(tabImage, borderWidth);

        // 2. Calculate dimensions
        var borderDoubleWidth = borderWidth * 2;
        int tabWidth = (int) tabImage.getWidth();
        int tabHeight = (int) tabImage.getHeight();
        int contentWidth = (int) contentImage.getWidth();
        int contentHeight = (int) contentImage.getHeight();

        // Overall image dimensions
        int imageWidth = Math.max(tabWidth, contentWidth) + borderDoubleWidth;
        int imageHeight = tabHeight + contentHeight + borderDoubleWidth;

        // 3. Create canvas with transparent background
        Canvas canvas = new Canvas(imageWidth, imageHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 4. Draw tab header (left-aligned)
        gc.drawImage(tabImage, borderWidth, borderWidth);
        // 5. Draw content (centered if wider than tab)
        gc.drawImage(contentImage, borderWidth, borderWidth + tabHeight);
        // 6. Draw complex border (notched right side)
        gc.setStroke(borderColor);
        gc.setLineWidth(borderWidth);
        // Top border (left side only - up to tab width)
        gc.strokeLine(borderWidth, borderWidth/2.0, borderWidth + tabWidth, borderWidth/2.0);

        // Right notch border (3 parts)
        // Vertical down from top to tab bottom
        gc.strokeLine(borderWidth + tabWidth + borderWidth/2.0, borderWidth/2.0,
                     borderWidth + tabWidth + borderWidth/2.0, borderWidth + tabHeight - borderWidth/2.0);
        // Horizontal right to content edge
        gc.strokeLine(borderWidth + tabWidth + borderWidth/2.0, borderWidth + tabHeight - borderWidth/2.0,
                     borderWidth + Math.max(tabWidth, contentWidth) + borderWidth/2.0,
                     borderWidth + tabHeight - borderWidth/2.0);
        // Vertical down to bottom
        gc.strokeLine(borderWidth + Math.max(tabWidth, contentWidth) + borderWidth/2.0,
                     borderWidth + tabHeight - borderWidth/2.0,
                     borderWidth + Math.max(tabWidth, contentWidth) + borderWidth/2.0,
                     borderWidth + tabHeight + contentHeight + borderWidth/2.0);
        // Left border (full height)
        gc.strokeLine(borderWidth/2.0, borderWidth/2.0,
                     borderWidth/2.0, borderWidth + tabHeight + contentHeight + borderWidth/2.0);
        // Bottom border
        gc.strokeLine(borderWidth/2.0, borderWidth + tabHeight + contentHeight + borderWidth/2.0,
                     borderWidth + Math.max(tabWidth, contentWidth) + borderWidth/2.0,
                     borderWidth + tabHeight + contentHeight + borderWidth/2.0);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.setOpacity(0.75);
        var result = canvas.snapshot(params, null);
        return result;
    }

    private WritableImage createTabImage(StackPane tabNode, int borderWidth) {
        var borderDoubleWidth = borderWidth * 2;
        var tabParams = new SnapshotParameters();
        if (tabNode.getWidth() + borderDoubleWidth > TAB_IMAGE_SIZE) {
            // cropping
            var viewPort = new Rectangle2D(0, 0, TAB_IMAGE_SIZE - borderDoubleWidth, tabNode.getHeight());
            tabParams.setViewport(viewPort);
        }
        WritableImage tabImage = tabNode.snapshot(tabParams, null);
        return tabImage;
    }

    private WritableImage createContentImage(WritableImage tabImage, int borderWidth) {
        var borderDoubleWidth = borderWidth * 2;
        Tab tab = getSelectionModel().getSelectedItem();
        var contentParams = new SnapshotParameters();
        // scaling
        Region content = (Region) tab.getContent();
        var k = (TAB_IMAGE_SIZE - borderDoubleWidth) / content.getWidth();
        var scale = new Scale(k, k);
        contentParams.setTransform(scale);
        WritableImage contentImage = content.snapshot(contentParams, null);

        // when performing both scaling and cropping on an image via ImageView, cropping and scaling may behave
        // unexpectedly (white BG color) if the content is shorter than the target dimensions after scaling
        ImageView imageView = new ImageView(contentImage);
        var imageViewParams = new SnapshotParameters();
        var viewPort = new Rectangle2D(0, 0, TAB_IMAGE_SIZE - borderDoubleWidth,
                Math.min(TAB_IMAGE_SIZE - borderDoubleWidth - tabImage.getHeight(), content.getHeight() / k));
        imageViewParams.setViewport(viewPort);
        contentImage = imageView.snapshot(imageViewParams, null);
        return contentImage;
    }
}
