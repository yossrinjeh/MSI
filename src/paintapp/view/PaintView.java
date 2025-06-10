package paintapp.view;

import paintapp.controller.PaintController;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

public class PaintView {
    private BorderPane root;
    private Canvas canvas;
    private PaintController controller;
    private Label statusLabel;
    private Stage parentStage;

    public PaintView(Stage parentStage) {
        this.parentStage = parentStage;
        root = new BorderPane();

        // Apply modern styling to root
        root.setStyle("-fx-background-color: #f8f9fa;");

        // Create canvas with modern styling
        canvas = new Canvas(1200, 900);
        canvas.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1px;");

        // Add drop shadow effect to canvas
        DropShadow canvasShadow = new DropShadow();
        canvasShadow.setRadius(8.0);
        canvasShadow.setOffsetX(2.0);
        canvasShadow.setOffsetY(2.0);
        canvasShadow.setColor(Color.color(0, 0, 0, 0.1));
        canvas.setEffect(canvasShadow);

        // Create canvas container with padding
        VBox canvasContainer = new VBox();
        canvasContainer.setAlignment(Pos.CENTER);
        canvasContainer.setPadding(new Insets(20));
        canvasContainer.getChildren().add(canvas);

        controller = new PaintController(canvas);
        controller.setParentStage(parentStage);
        root.setCenter(canvasContainer);

        // Create menu bar
        createMenuBar();

        // Create toolbar
        createToolbar();

        // Create status bar
        createStatusBar();

        // Event handlers
        controller.initEvents();

        // Set up status updates
        controller.setStatusLabel(statusLabel);
    }

    private void createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Modern menu bar styling
        menuBar.setStyle(
            "-fx-background-color: #ffffff; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 0 0 1 0; " +
            "-fx-padding: 5 10 5 10;"
        );

        // File menu
        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");

        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");

        // Style menu items
        String menuItemStyle = "-fx-font-size: 13px; -fx-padding: 8 15 8 15;";
        newItem.setStyle(menuItemStyle);
        openItem.setStyle(menuItemStyle);
        saveItem.setStyle(menuItemStyle);
        saveAsItem.setStyle(menuItemStyle);
        exitItem.setStyle(menuItemStyle);

        // Add menu items to file menu
        fileMenu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(),
                                  saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        // Add event handlers (placeholder for now)
        newItem.setOnAction(e -> controller.newDrawing());
        openItem.setOnAction(e -> controller.openDrawing());
        saveItem.setOnAction(e -> controller.saveDrawing());
        saveAsItem.setOnAction(e -> controller.saveAsDrawing());
        exitItem.setOnAction(e -> controller.exitApplication());

        // Settings menu for logging options
        Menu settingsMenu = new Menu("Settings");
        settingsMenu.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");

        Menu loggingMenu = new Menu("Logging Method");
        loggingMenu.setStyle("-fx-font-size: 13px;");

        MenuItem consoleLoggingItem = new MenuItem("Console Logging");
        MenuItem fileLoggingItem = new MenuItem("File Logging");
        MenuItem databaseLoggingItem = new MenuItem("Database Logging");

        // Style logging menu items
        consoleLoggingItem.setStyle(menuItemStyle);
        fileLoggingItem.setStyle(menuItemStyle);
        databaseLoggingItem.setStyle(menuItemStyle);

        // Add logging menu items
        loggingMenu.getItems().addAll(consoleLoggingItem, fileLoggingItem, databaseLoggingItem);
        settingsMenu.getItems().add(loggingMenu);

        // Add event handlers for logging methods
        consoleLoggingItem.setOnAction(e -> controller.switchToConsoleLogging());
        fileLoggingItem.setOnAction(e -> controller.switchToFileLogging());
        databaseLoggingItem.setOnAction(e -> controller.switchToDatabaseLogging());

        menuBar.getMenus().addAll(fileMenu, settingsMenu);
        root.setTop(menuBar);
    }

    private void createToolbar() {
        VBox topContainer = new VBox();

        // Get existing menu bar
        MenuBar menuBar = (MenuBar) root.getTop();

        // Create modern toolbar
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(12, 20, 12, 20));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa); " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 0 0 1 0; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);"
        );

        // Modern button styling
        String buttonStyle =
            "-fx-background-color: linear-gradient(to bottom, #ffffff, #f8f9fa); " +
            "-fx-border-color: #6c757d; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 6px; " +
            "-fx-background-radius: 6px; " +
            "-fx-padding: 8 16 8 16; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 500; " +
            "-fx-cursor: hand;";

        String buttonHoverStyle =
            "-fx-background-color: linear-gradient(to bottom, #e9ecef, #dee2e6); " +
            "-fx-border-color: #495057; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 6px; " +
            "-fx-background-radius: 6px; " +
            "-fx-padding: 8 16 8 16; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 500; " +
            "-fx-cursor: hand;";

        // Undo/Redo buttons with modern styling
        Button undoBtn = new Button("↶ Undo");
        Button redoBtn = new Button("↷ Redo");

        undoBtn.setStyle(buttonStyle);
        redoBtn.setStyle(buttonStyle);

        // Add hover effects
        undoBtn.setOnMouseEntered(e -> undoBtn.setStyle(buttonHoverStyle));
        undoBtn.setOnMouseExited(e -> undoBtn.setStyle(buttonStyle));
        redoBtn.setOnMouseEntered(e -> redoBtn.setStyle(buttonHoverStyle));
        redoBtn.setOnMouseExited(e -> redoBtn.setStyle(buttonStyle));

        // Shape selector with modern styling
        ComboBox<String> shapeSelector = new ComboBox<>();
        shapeSelector.getItems().addAll("Line", "Rectangle", "Ellipse");
        shapeSelector.setValue("Line");
        shapeSelector.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #6c757d; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 6px; " +
            "-fx-background-radius: 6px; " +
            "-fx-padding: 6 12 6 12; " +
            "-fx-font-size: 13px; " +
            "-fx-min-width: 120px;"
        );

        // Color picker with modern styling and label
        VBox colorSection = new VBox(5);
        colorSection.setAlignment(Pos.CENTER);
        Label colorLabel = new Label("Color");
        colorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: #495057;");

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setStyle(
            "-fx-color-label-visible: false; " +
            "-fx-background-radius: 6px; " +
            "-fx-border-radius: 6px; " +
            "-fx-border-color: #6c757d; " +
            "-fx-border-width: 1px; " +
            "-fx-min-width: 60px; " +
            "-fx-min-height: 35px;"
        );

        colorSection.getChildren().addAll(colorLabel, colorPicker);

        // Fill/Stroke options with modern styling
        CheckBox fillCheckBox = new CheckBox("Fill Mode");
        fillCheckBox.setSelected(false); // Default to stroke
        fillCheckBox.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 500; " +
            "-fx-text-fill: #495057;"
        );

        // Add event handlers (preserving all existing functionality)
        undoBtn.setOnAction(e -> controller.undo());
        redoBtn.setOnAction(e -> controller.redo());
        shapeSelector.setOnAction(e -> controller.setCurrentShape(shapeSelector.getValue()));
        colorPicker.setOnAction(e -> controller.setCurrentColor(colorPicker.getValue()));
        fillCheckBox.setOnAction(e -> controller.setFillMode(fillCheckBox.isSelected()));

        // Create modern separators
        Region separator1 = new Region();
        separator1.setPrefWidth(1);
        separator1.setPrefHeight(30);
        separator1.setStyle("-fx-background-color: #dee2e6;");

        Region separator2 = new Region();
        separator2.setPrefWidth(1);
        separator2.setPrefHeight(30);
        separator2.setStyle("-fx-background-color: #dee2e6;");

        Region separator3 = new Region();
        separator3.setPrefWidth(1);
        separator3.setPrefHeight(30);
        separator3.setStyle("-fx-background-color: #dee2e6;");

        // Add all controls to toolbar with better spacing
        toolbar.getChildren().addAll(
            undoBtn, redoBtn, separator1,
            new Label("Shape:") {{ setStyle("-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: #495057;"); }},
            shapeSelector, separator2,
            colorSection, separator3,
            fillCheckBox
        );

        // Combine menu bar and toolbar
        topContainer.getChildren().addAll(menuBar, toolbar);
        root.setTop(topContainer);
    }

    private void createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(10, 20, 10, 20));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef); " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-width: 1 0 0 0; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.03), 2, 0, 0, -1);"
        );

        statusLabel = new Label("Ready - Select a tool and start drawing");
        statusLabel.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-font-weight: 400; " +
            "-fx-text-fill: #495057; " +
            "-fx-padding: 2 0 2 0;"
        );

        // Add a small status indicator
        Region statusIndicator = new Region();
        statusIndicator.setPrefSize(8, 8);
        statusIndicator.setStyle(
            "-fx-background-color: #28a745; " +
            "-fx-background-radius: 50%; " +
            "-fx-border-radius: 50%;"
        );

        HBox statusContent = new HBox(8);
        statusContent.setAlignment(Pos.CENTER_LEFT);
        statusContent.getChildren().addAll(statusIndicator, statusLabel);

        statusBar.getChildren().add(statusContent);
        root.setBottom(statusBar);
    }

    public BorderPane getRoot() {
        return root;
    }
}
