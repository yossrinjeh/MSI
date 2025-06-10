package paintapp.view;

import paintapp.controller.PaintController;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class PaintView {
    private BorderPane root;
    private Canvas canvas;
    private PaintController controller;
    private Label statusLabel;
    private Stage parentStage;

    public PaintView(Stage parentStage) {
        this.parentStage = parentStage;
        root = new BorderPane();
        canvas = new Canvas(800, 600);
        controller = new PaintController(canvas);
        controller.setParentStage(parentStage);
        root.setCenter(canvas);

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

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");

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
        Menu loggingMenu = new Menu("Logging Method");

        MenuItem consoleLoggingItem = new MenuItem("Console Logging");
        MenuItem fileLoggingItem = new MenuItem("File Logging");
        MenuItem databaseLoggingItem = new MenuItem("Database Logging");

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

        // Create toolbar
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(5));

        // Undo/Redo buttons
        Button undoBtn = new Button("Undo");
        Button redoBtn = new Button("Redo");

        // Shape selector
        ComboBox<String> shapeSelector = new ComboBox<>();
        shapeSelector.getItems().addAll("Line", "Rectangle", "Ellipse");
        shapeSelector.setValue("Line");

        // Color picker
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        Label colorLabel = new Label("Color:");

        // Fill/Stroke options
        CheckBox fillCheckBox = new CheckBox("Fill");
        fillCheckBox.setSelected(false); // Default to stroke

        // Add event handlers
        undoBtn.setOnAction(e -> controller.undo());
        redoBtn.setOnAction(e -> controller.redo());
        shapeSelector.setOnAction(e -> controller.setCurrentShape(shapeSelector.getValue()));
        colorPicker.setOnAction(e -> controller.setCurrentColor(colorPicker.getValue()));
        fillCheckBox.setOnAction(e -> controller.setFillMode(fillCheckBox.isSelected()));

        // Add all controls to toolbar
        toolbar.getChildren().addAll(undoBtn, redoBtn, new Separator(),
                                   shapeSelector, new Separator(),
                                   colorLabel, colorPicker, new Separator(),
                                   fillCheckBox);

        // Combine menu bar and toolbar
        topContainer.getChildren().addAll(menuBar, toolbar);
        root.setTop(topContainer);
    }

    private void createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");

        statusLabel = new Label("Ready - Select a tool and start drawing");
        statusBar.getChildren().add(statusLabel);

        root.setBottom(statusBar);
    }

    public BorderPane getRoot() {
        return root;
    }
}
