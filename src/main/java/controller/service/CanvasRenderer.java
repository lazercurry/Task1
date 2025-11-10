package controller.service;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.AlgorithmConstants;
import java.util.List;

public class CanvasRenderer {
    private GraphicsContext gc;

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void clear(double width, double height) {
        gc.clearRect(0, 0, width, height);
    }

    public void drawVertices(List<Point2D> vertices) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", 12.0));
        gc.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < vertices.size(); i++) {
            Point2D p = vertices.get(i);
            gc.setFill(Color.RED);
            double r = AlgorithmConstants.POINT_RADIUS;
            gc.fillOval(p.getX() - r, p.getY() - r, AlgorithmConstants.POINT_DIAMETER, AlgorithmConstants.POINT_DIAMETER);
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(i + 1), p.getX(), p.getY() - (r + 3));
        }
    }
}