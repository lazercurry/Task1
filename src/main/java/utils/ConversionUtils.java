package utils;

import javafx.geometry.Point2D;
import java.awt.Point;
import java.awt.Color;

public final class ConversionUtils {
    public static Point toAwtPoint(Point2D fxPoint) {
        return new Point((int) fxPoint.getX(), (int) fxPoint.getY());
    }

    public static Color toAwtColor(javafx.scene.paint.Color fxColor) {
        return new Color(
                (float) fxColor.getRed(),
                (float) fxColor.getGreen(),
                (float) fxColor.getBlue()
        );
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private ConversionUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
}