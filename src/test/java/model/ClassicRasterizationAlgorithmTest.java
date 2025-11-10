package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

class ClassicRasterizationAlgorithmTest {

    private SolidFillAlgorithm algorithm;
    private Color testColor;
    private Point p1, p2, p3;
    private int canvasWidth, canvasHeight;

    @BeforeEach
    void setUp() {
        testColor = Color.RED;
        p1 = new Point(10, 10);
        p2 = new Point(50, 10);
        p3 = new Point(30, 40);
        canvasWidth = 100;
        canvasHeight = 100;

        algorithm = new SolidFillAlgorithm(testColor, p1, p2, p3, canvasWidth, canvasHeight);
    }

    @Test
    @DisplayName("Конструктор задаёт базовые параметры без null")
    void constructor_ShouldInitializeNonNull() {
        assertNotNull(algorithm);
        assertNotNull(getFieldValue(algorithm, "baseColor"));
        assertNotNull(getFieldValue(algorithm, "v1"));
        assertNotNull(getFieldValue(algorithm, "v2"));
        assertNotNull(getFieldValue(algorithm, "v3"));
    }

    @Test
    @DisplayName("calculatePixelColor должен всегда возвращать базовый цвет")
    void calculatePixelColor_ShouldAlwaysReturnBaseColor() {
        double w1 = 0.3;
        double w2 = 0.3;
        double w3 = 0.4;

        Color result = invokeCalculatePixelColor(algorithm, w1, w2, w3);

        assertEquals(testColor, result);
    }

    @Test
    @DisplayName("calculatePixelColor должен возвращать тот же цвет для разных барицентрических координат")
    void calculatePixelColor_ShouldReturnSameColor_ForDifferentBarycentricCoordinates() {
        Color result1 = invokeCalculatePixelColor(algorithm, 1.0, 0.0, 0.0);
        Color result2 = invokeCalculatePixelColor(algorithm, 0.0, 1.0, 0.0);
        Color result3 = invokeCalculatePixelColor(algorithm, 0.0, 0.0, 1.0);
        Color result4 = invokeCalculatePixelColor(algorithm, 0.33, 0.33, 0.34);

        assertEquals(testColor, result1);
        assertEquals(testColor, result2);
        assertEquals(testColor, result3);
        assertEquals(testColor, result4);
    }

    @Test
    @DisplayName("Алгоритм должен работать с разными цветами")
    void algorithm_ShouldWorkWithDifferentColors() {
        Color blueColor = Color.BLUE;
        SolidFillAlgorithm blueAlgorithm =
                new SolidFillAlgorithm(blueColor, p1, p2, p3, canvasWidth, canvasHeight);

        Color result = invokeCalculatePixelColor(blueAlgorithm, 0.5, 0.3, 0.2);

        assertEquals(blueColor, result);
    }

    @Test
    @DisplayName("Алгоритм должен корректно обрабатывать граничные случаи координат")
    void algorithm_ShouldHandleBoundaryCoordinates() {
        Point edge1 = new Point(0, 0);
        Point edge2 = new Point(canvasWidth - 1, 0);
        Point edge3 = new Point(0, canvasHeight - 1);

        SolidFillAlgorithm edgeAlgorithm =
                new SolidFillAlgorithm(testColor, edge1, edge2, edge3, canvasWidth, canvasHeight);

        Color result = invokeCalculatePixelColor(edgeAlgorithm, 0.33, 0.33, 0.34);
        assertEquals(testColor, result);
    }

    @Test
    @DisplayName("Алгоритм должен работать с вырожденными треугольниками")
    void algorithm_ShouldWorkWithDegenerateTriangles() {
        Point col1 = new Point(0, 0);
        Point col2 = new Point(10, 10);
        Point col3 = new Point(20, 20);

        SolidFillAlgorithm degenerateAlgorithm =
                new SolidFillAlgorithm(testColor, col1, col2, col3, canvasWidth, canvasHeight);

        Color result = invokeCalculatePixelColor(degenerateAlgorithm, 0.5, 0.5, 0.0);
        assertEquals(testColor, result);
    }

    @Test
    @DisplayName("Алгоритм должен сохранять состояние после создания")
    void algorithm_ShouldMaintainStateAfterConstruction() {
        Color newColor = Color.GREEN;
        Point newP1 = new Point(5, 5);
        Point newP2 = new Point(15, 5);
        Point newP3 = new Point(10, 15);
        int newWidth = 50;
        int newHeight = 50;

        SolidFillAlgorithm newAlgorithm =
                new SolidFillAlgorithm(newColor, newP1, newP2, newP3, newWidth, newHeight);

        Color result = invokeCalculatePixelColor(newAlgorithm, 0.25, 0.25, 0.5);
        assertEquals(newColor, result);
    }

    @Test
    @DisplayName("Алгоритм должен корректно создаваться с валидными параметрами")
    void algorithm_ShouldCreateWithValidParameters() {
        assertDoesNotThrow(() -> {
            new SolidFillAlgorithm(Color.BLACK,
                    new Point(1, 1), new Point(2, 2), new Point(1, 3), 10, 10);
        });

        assertDoesNotThrow(() -> {
            new SolidFillAlgorithm(new Color(128, 128, 128),
                    new Point(0, 0), new Point(99, 0), new Point(50, 99), 100, 100);
        });
    }

    private Color invokeCalculatePixelColor(SolidFillAlgorithm algo, double w1, double w2, double w3) {
        try {
            java.lang.reflect.Method method = algo.getClass()
                    .getDeclaredMethod("computePixelColor", double.class, double.class, double.class);
            method.setAccessible(true);
            return (Color) method.invoke(algo, w1, w2, w3);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke computePixelColor method", e);
        }
    }

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException e) {
            try {
                java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to access field: " + fieldName, ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + fieldName, e);
        }
    }
}