package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

class InterpolatedRasterizationAlgorithmTest {

    private GradientFillAlgorithm algorithm;
    private Color color1, color2, color3;
    private Point p1, p2, p3;
    private int canvasWidth, canvasHeight;

    @BeforeEach
    void setUp() {
        color1 = Color.RED;
        color2 = Color.GREEN;
        color3 = Color.BLUE;
        p1 = new Point(10, 10);
        p2 = new Point(50, 10);
        p3 = new Point(30, 40);
        canvasWidth = 100;
        canvasHeight = 100;

        algorithm = new GradientFillAlgorithm(
                color1, color2, color3, p1, p2, p3, canvasWidth, canvasHeight);
    }

    @Test
    @DisplayName("Конструктор бросает исключение при null дополнительных цветах")
    void constructor_ShouldThrowException_WhenAdditionalColorsAreNull() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> new GradientFillAlgorithm(color1, null, color3, p1, p2, p3, canvasWidth, canvasHeight));
        assertEquals("Vertex colors must not be null", ex1.getMessage());
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> new GradientFillAlgorithm(color1, color2, null, p1, p2, p3, canvasWidth, canvasHeight));
        assertEquals("Vertex colors must not be null", ex2.getMessage());
    }

    @Test
    @DisplayName("calculatePixelColor должен корректно интерполировать цвета в вершинах")
    void calculatePixelColor_ShouldInterpolateColorsAtVertices() {
        Color result1 = invokeCalculatePixelColor(algorithm, 1.0, 0.0, 0.0);
        assertEquals(color1, result1);

        Color result2 = invokeCalculatePixelColor(algorithm, 0.0, 1.0, 0.0);
        assertEquals(color2, result2);

        Color result3 = invokeCalculatePixelColor(algorithm, 0.0, 0.0, 1.0);
        assertEquals(color3, result3);
    }

    @Test
    @DisplayName("calculatePixelColor должен корректно интерполировать цвета в центре")
    void calculatePixelColor_ShouldInterpolateColorsAtCenter() {
        Color result = invokeCalculatePixelColor(algorithm, 0.33, 0.33, 0.34);

        int expectedRed = (int) Math.round(0.33 * color1.getRed() + 0.33 * color2.getRed() + 0.34 * color3.getRed());
        int expectedGreen = (int) Math.round(0.33 * color1.getGreen() + 0.33 * color2.getGreen() + 0.34 * color3.getGreen());
        int expectedBlue = (int) Math.round(0.33 * color1.getBlue() + 0.33 * color2.getBlue() + 0.34 * color3.getBlue());

        assertEquals(expectedRed, result.getRed());
        assertEquals(expectedGreen, result.getGreen());
        assertEquals(expectedBlue, result.getBlue());
    }


    @Test
    @DisplayName("calculatePixelColor должен корректно работать с отрицательными весами (близкими к нулю)")
    void calculatePixelColor_ShouldHandleNegativeWeightsNearZero() {
        Color result = invokeCalculatePixelColor(algorithm, -0.0001, 0.5, 0.5001);

        assertNotNull(result);
        assertTrue(result.getRed() >= 0 && result.getRed() <= 255);
        assertTrue(result.getGreen() >= 0 && result.getGreen() <= 255);
        assertTrue(result.getBlue() >= 0 && result.getBlue() <= 255);
    }


    @Test
    @DisplayName("Алгоритм должен корректно работать с граничными значениями цветов")
    void algorithm_ShouldWorkWithBoundaryColorValues() {
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        Color gray = Color.GRAY;

        GradientFillAlgorithm bwAlgorithm =
                new GradientFillAlgorithm(black, white, gray, p1, p2, p3, canvasWidth, canvasHeight);

        Color result = invokeCalculatePixelColor(bwAlgorithm, 0.5, 0.3, 0.2);

        assertNotNull(result);
        assertTrue(result.getRed() >= 0 && result.getRed() <= 255);
        assertTrue(result.getGreen() >= 0 && result.getGreen() <= 255);
        assertTrue(result.getBlue() >= 0 && result.getBlue() <= 255);
    }


    @Test
    @DisplayName("Интерполяция должна быть линейной")
    void interpolation_ShouldBeLinear() {
        Color result1 = invokeCalculatePixelColor(algorithm, 0.5, 0.5, 0.0);
        Color result2 = invokeCalculatePixelColor(algorithm, 0.25, 0.75, 0.0);

        int r1 = result1.getRed();
        int r2 = result2.getRed();

        int expectedR = (int) Math.round(0.25 * color1.getRed() + 0.75 * color2.getRed());
        assertEquals(expectedR, r2);
    }

    private Color invokeCalculatePixelColor(GradientFillAlgorithm algo, double w1, double w2, double w3) {
        try {
            java.lang.reflect.Method method = algo.getClass()
                    .getDeclaredMethod("computePixelColor", double.class, double.class, double.class);
            method.setAccessible(true);
            return (Color) method.invoke(algo, w1, w2, w3);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke computePixelColor method", e);
        }
    }
}
