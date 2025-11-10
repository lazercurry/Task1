package controller;

import controller.service.ColorManager;
import controller.service.CanvasRenderer;
import controller.service.VertexManager;
import controller.service.TriangleFiller;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.FillMode;
import model.AlgorithmConstants;
import model.factory.StandardFillAlgorithmFactory;

public class MainViewController {

    @FXML private Canvas canvas;
    @FXML private AnchorPane canvasContainer;
    @FXML private ComboBox<FillMode> fillModeSelector;
    @FXML private Label vertexColorLabel1;
    @FXML private ColorPicker vertexColor1;
    @FXML private Label vertexColorLabel2;
    @FXML private ColorPicker vertexColor2;
    @FXML private Label vertexColorLabel3;
    @FXML private ColorPicker vertexColor3;
    @FXML private Button drawButton;
    @FXML private Button clearButton;

    private GraphicsContext gc;
    private VertexManager vertexManager;
    private CanvasRenderer canvasRenderer;
    private TriangleFiller triangleFiller;
    private ColorManager colorManager;

    public MainViewController() {}

    @FXML
    private void initialize() {
        vertexManager = new VertexManager();
        canvasRenderer = new CanvasRenderer();
        triangleFiller = new TriangleFiller(new StandardFillAlgorithmFactory());
        colorManager = new ColorManager();

        gc = canvas.getGraphicsContext2D();
        canvasRenderer.setGraphicsContext(gc);

        canvas.setWidth(600);
        canvas.setHeight(500);

        canvas.setOnMouseClicked(this::handleMouseClick);

        setupUI();
        setupEventHandlers();
        updateColorPickersVisibility();
    }

    private void setupUI() {
        fillModeSelector.getItems().addAll(FillMode.values());
        fillModeSelector.setValue(FillMode.SOLID);
        vertexColor1.setValue(Color.RED);
        vertexColor2.setValue(Color.GREEN);
        vertexColor3.setValue(Color.BLUE);
    }

    private void setupEventHandlers() {
        fillModeSelector.setOnAction(e -> updateColorPickersVisibility());
        drawButton.setOnAction(e -> drawTriangle());
        clearButton.setOnAction(e -> clearCanvas());
    }

    private void updateColorPickersVisibility() {
        FillMode mode = fillModeSelector.getValue();
        boolean isGradient = mode == FillMode.GRADIENT;
        vertexColorLabel2.setVisible(isGradient);
        vertexColor2.setVisible(isGradient);
        vertexColorLabel3.setVisible(isGradient);
        vertexColor3.setVisible(isGradient);
    }

    private void clearCanvas() {
        canvasRenderer.clear(canvas.getWidth(), canvas.getHeight());
        vertexManager.clearVertices();
    }

    private void drawTriangle() {
        if (!vertexManager.hasEnoughVertices()) return;

        FillMode mode = fillModeSelector.getValue();
        java.awt.Color[] colors = colorManager.prepareColors(mode, vertexColor1, vertexColor2, vertexColor3);
        triangleFiller.fillTriangle(canvas, vertexManager.getVertices(), mode, colors);
    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if (event.getButton().toString().equals("PRIMARY")) {
            addVertex(x, y);
        } else if (event.getButton().toString().equals("SECONDARY")) {
            removeNearestVertex(x, y);
        }
    }

    private void addVertex(double x, double y) {
        if (vertexManager.addVertex(x, y)) {
            redrawCanvas();
        }
    }

    private void removeNearestVertex(double x, double y) {
        if (vertexManager.removeNearestVertex(x, y, AlgorithmConstants.POINT_DELETE_RADIUS)) {
            redrawCanvas();
        }
    }

    private void redrawCanvas() {
        canvasRenderer.clear(canvas.getWidth(), canvas.getHeight());
        if (vertexManager.hasEnoughVertices()) {
            drawTriangle();
        }
        canvasRenderer.drawVertices(vertexManager.getVertices());
    }


    public GraphicsContext getGraphicsContext() {
        return gc;
    }
}