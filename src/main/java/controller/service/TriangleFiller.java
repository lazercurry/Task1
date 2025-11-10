package controller.service;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import model.FillMode;
import model.Rasterization;
import model.factory.FillAlgorithmFactory;
import utils.ConversionUtils;
import java.awt.Color;
import java.awt.Point;
import java.util.List;

public class TriangleFiller {
    private final FillAlgorithmFactory factory;

    public TriangleFiller(FillAlgorithmFactory factory) {
        this.factory = factory;
    }

    public boolean fillTriangle(Canvas canvas, List<Point2D> vertices, FillMode mode, Color[] colors) {
        if (vertices.size() < 3) return false;
        WritableImage img = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        var pw = img.getPixelWriter();
        Point p1 = ConversionUtils.toAwtPoint(vertices.get(0));
        Point p2 = ConversionUtils.toAwtPoint(vertices.get(1));
        Point p3 = ConversionUtils.toAwtPoint(vertices.get(2));
        int w = (int) canvas.getWidth();
        int h = (int) canvas.getHeight();

        Rasterization algorithm;
        if (mode == FillMode.SOLID) {
            if (colors == null || colors.length < 1)
                throw new IllegalArgumentException("Solid fill requires 1 color");
            algorithm = factory.createSolidAlgorithm(colors[0], p1, p2, p3, w, h);
        } else {
            if (colors == null || colors.length < 3)
                throw new IllegalArgumentException("Gradient fill requires 3 colors");
            algorithm = factory.createGradientAlgorithm(colors[0], colors[1], colors[2], p1, p2, p3, w, h);
        }

        algorithm.rasterize(pw);
        canvas.getGraphicsContext2D().drawImage(img, 0, 0);
        return true;
    }
}