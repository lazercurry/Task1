package model.factory;

import model.Rasterization;
import java.awt.Color;
import java.awt.Point;

public interface FillAlgorithmFactory {
    Rasterization createSolidAlgorithm(Color color, Point v1, Point v2, Point v3, int w, int h);
    Rasterization createGradientAlgorithm(Color c1, Color c2, Color c3, Point v1, Point v2, Point v3, int w, int h);
}