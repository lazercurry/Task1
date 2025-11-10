package model;

import javafx.scene.image.PixelWriter;
import java.awt.Color;
import java.awt.Point;

public abstract class RasterizationAlgorithm implements Rasterization {
    protected Color baseColor;
    protected Point v1, v2, v3;
    protected int canvasWidth, canvasHeight;

    public RasterizationAlgorithm(Color baseColor, Point v1, Point v2, Point v3, int w, int h) {
        if (baseColor == null || v1 == null || v2 == null || v3 == null || w <= 0 || h <= 0)
            throw new IllegalArgumentException("Invalid parameters");
        this.baseColor = baseColor;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.canvasWidth = w;
        this.canvasHeight = h;
    }

    @Override
    public void rasterize(PixelWriter pw) {
        double[] bbox = getBoundingBox();
        double area = computeSignedArea(v1, v2, v3);
        if (area < 0) {
            swapVertices();
            area = -area;
        }
        double invArea = 1.0 / area;

        int startY = (int) bbox[1];
        int endY = (int) bbox[3];
        int startX = (int) bbox[0];
        int endX = (int) bbox[2];

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                double px = x + 0.5;
                double py = y + 0.5;
                double w1 = computeSignedArea(v2, v3, new Point((int) px, (int) py)) * invArea;
                double w2 = computeSignedArea(v3, v1, new Point((int) px, (int) py)) * invArea;
                double w3 = computeSignedArea(v1, v2, new Point((int) px, (int) py)) * invArea;
                if (w1 >= -AlgorithmConstants.EPSILON &&
                        w2 >= -AlgorithmConstants.EPSILON &&
                        w3 >= -AlgorithmConstants.EPSILON) {
                    Color pixelColor = computePixelColor(w1, w2, w3);
                    pw.setArgb(x, y, pixelColor.getRGB());
                }
            }
        }
    }

    protected abstract Color computePixelColor(double w1, double w2, double w3);

    protected void swapVertices() {
        Point tmp = v2;
        v2 = v3;
        v3 = tmp;
        onVerticesSwapped();
    }

    protected void onVerticesSwapped() {}

    private double[] getBoundingBox() {
        double minX = Math.max(0, Math.floor(Math.min(v1.x, Math.min(v2.x, v3.x))));
        double minY = Math.max(0, Math.floor(Math.min(v1.y, Math.min(v2.y, v3.y))));
        double maxX = Math.min(canvasWidth - 1, Math.ceil(Math.max(v1.x, Math.max(v2.x, v3.x))));
        double maxY = Math.min(canvasHeight - 1, Math.ceil(Math.max(v1.y, Math.max(v2.y, v3.y))));
        return new double[]{minX, minY, maxX, maxY};
    }

    private double computeSignedArea(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }
}