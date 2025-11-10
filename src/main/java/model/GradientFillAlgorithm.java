package model;

import java.awt.Color;
import java.awt.Point;
import utils.ConversionUtils;

public class GradientFillAlgorithm extends RasterizationAlgorithm {
    private Color color2, color3;

    public GradientFillAlgorithm(Color c1, Color c2, Color c3, Point v1, Point v2, Point v3, int w, int h) {
        super(c1, v1, v2, v3, w, h);
        if (c2 == null || c3 == null)
            throw new IllegalArgumentException("Vertex colors must not be null");
        this.color2 = c2;
        this.color3 = c3;
    }

    @Override
    protected Color computePixelColor(double w1, double w2, double w3) {
        int r = (int) Math.round(w1 * baseColor.getRed()   + w2 * color2.getRed()   + w3 * color3.getRed());
        int g = (int) Math.round(w1 * baseColor.getGreen() + w2 * color2.getGreen() + w3 * color3.getGreen());
        int b = (int) Math.round(w1 * baseColor.getBlue()  + w2 * color2.getBlue()  + w3 * color3.getBlue());
        r = ConversionUtils.clamp(r, AlgorithmConstants.COLOR_MIN, AlgorithmConstants.COLOR_MAX);
        g = ConversionUtils.clamp(g, AlgorithmConstants.COLOR_MIN, AlgorithmConstants.COLOR_MAX);
        b = ConversionUtils.clamp(b, AlgorithmConstants.COLOR_MIN, AlgorithmConstants.COLOR_MAX);
        return new Color(r, g, b);
    }

    @Override
    protected void onVerticesSwapped() {
        Color tmp = color2;
        color2 = color3;
        color3 = tmp;
    }
}