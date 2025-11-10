package model;

public final class AlgorithmConstants {
    public static final int MAX_TRIANGLE_POINTS = 3;
    public static final int POINT_RADIUS = 5;
    public static final int POINT_DIAMETER = 10;
    public static final int POINT_DELETE_RADIUS = 20;
    public static final double EPSILON = 1e-6;
    public static final int COLOR_MIN = 0;
    public static final int COLOR_MAX = 255;

    private AlgorithmConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}