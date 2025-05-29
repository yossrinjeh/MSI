package paintapp.view;

import paintapp.controller.PaintController;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class PaintView {
    private BorderPane root;
    private Canvas canvas;
    private PaintController controller;

    public PaintView() {
        root = new BorderPane();
        canvas = new Canvas(800, 600);
        controller = new PaintController(canvas);
        root.setCenter(canvas);

        // Top toolbar
        HBox toolbar = new HBox();
        Button undoBtn = new Button("Undo");
        Button redoBtn = new Button("Redo");
        ComboBox<String> shapeSelector = new ComboBox<>();
        shapeSelector.getItems().addAll("Line", "Rectangle", "Ellipse");
        shapeSelector.setValue("Line");

        undoBtn.setOnAction(e -> controller.undo());
        redoBtn.setOnAction(e -> controller.redo());
        shapeSelector.setOnAction(e -> controller.setCurrentShape(shapeSelector.getValue()));

        toolbar.getChildren().addAll(undoBtn, redoBtn, shapeSelector);
        root.setTop(toolbar);

        // Event handlers
        controller.initEvents();
    }

    public BorderPane getRoot() {
        return root;
    }
}
