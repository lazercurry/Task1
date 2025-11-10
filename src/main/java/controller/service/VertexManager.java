package controller.service;

import javafx.geometry.Point2D;
import model.AlgorithmConstants;
import java.util.ArrayList;
import java.util.List;

public class VertexManager {
    private final List<Point2D> vertices = new ArrayList<>();

    public boolean addVertex(double x, double y) {
        if (vertices.size() >= AlgorithmConstants.MAX_TRIANGLE_POINTS) return false;
        vertices.add(new Point2D(x, y));
        return true;
    }

    public boolean removeNearestVertex(double x, double y, double radius) {
        if (vertices.isEmpty()) return false;
        Point2D nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D p : vertices) {
            double dx = p.getX() - x;
            double dy = p.getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < minDist) {
                minDist = dist;
                nearest = p;
            }
        }
        if (nearest != null && minDist < radius) {
            vertices.remove(nearest);
            return true;
        }
        return false;
    }

    public void clearVertices() {
        vertices.clear();
    }

    public List<Point2D> getVertices() {
        return new ArrayList<>(vertices);
    }

    public boolean hasEnoughVertices() {
        return vertices.size() >= AlgorithmConstants.MAX_TRIANGLE_POINTS;
    }
}