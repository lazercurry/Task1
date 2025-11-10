package model.factory;

import model.Rasterization;
import model.SolidFillAlgorithm;
import model.GradientFillAlgorithm;
import java.awt.Color;
import java.awt.Point;

public class StandardFillAlgorithmFactory implements FillAlgorithmFactory {
    @Override
    public Rasterization createSolidAlgorithm(Color color, Point v1, Point v2, Point v3, int w, int h) {
        return new SolidFillAlgorithm(color, v1, v2, v3, w, h);
    }

    @Override
    public Rasterization createGradientAlgorithm(Color c1, Color c2, Color c3, Point v1, Point v2, Point v3, int w, int h) {
        return new GradientFillAlgorithm(c1, c2, c3, v1, v2, v3, w, h);
    }
}