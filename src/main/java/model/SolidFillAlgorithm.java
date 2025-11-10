package model;

import java.awt.Color;
import java.awt.Point;

public class SolidFillAlgorithm extends RasterizationAlgorithm {
    public SolidFillAlgorithm(Color color, Point v1, Point v2, Point v3, int w, int h) {
        super(color, v1, v2, v3, w, h);
    }

    @Override
    protected Color computePixelColor(double w1, double w2, double w3) {
        return baseColor;
    }
}