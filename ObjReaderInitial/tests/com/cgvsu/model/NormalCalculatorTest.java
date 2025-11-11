package com.cgvsu.model;

import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class NormalCalculatorTest {

    @Test
    void testEmptyModel_NoVertices() {
        Model model = new Model();
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertTrue(model.getNormals().isEmpty());
    }

    @Test
    void testEmptyModel_NoPolygons() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(0,0,0));
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertTrue(model.getNormals().isEmpty());
    }

    @Test
    void testSingleTriangle_NormalUp() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(0,0,0));
        model.getVertices().add(new Vector3f(1,0,0));
        model.getVertices().add(new Vector3f(0,1,0));
        Polygon p = new Polygon();
        p.setVertexIndices(new ArrayList<>(Arrays.asList(0,1,2)));
        model.getPolygons().add(p);
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertEquals(3, model.getNormals().size());
        Vector3f expected = new Vector3f(0,0,1);
        for (Vector3f n : model.getNormals()) {
            Assertions.assertEquals(expected, n);
        }
    }

    @Test
    void testTwoTrianglesSharingEdge_AverageSamePlane() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(0,0,0));
        model.getVertices().add(new Vector3f(1,0,0));
        model.getVertices().add(new Vector3f(1,1,0));
        model.getVertices().add(new Vector3f(0,1,0));
        Polygon p1 = new Polygon();
        p1.setVertexIndices(new ArrayList<>(Arrays.asList(0,1,2)));
        Polygon p2 = new Polygon();
        p2.setVertexIndices(new ArrayList<>(Arrays.asList(0,2,3)));
        model.getPolygons().add(p1);
        model.getPolygons().add(p2);
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertEquals(4, model.getNormals().size());
        Vector3f expected = new Vector3f(0,0,1);
        for (Vector3f n : model.getNormals()) {
            Assertions.assertEquals(expected, n);
        }
    }

    @Test
    void testDegenerateTriangle_ZeroNormals() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(0,0,0));
        model.getVertices().add(new Vector3f(1,0,0));
        model.getVertices().add(new Vector3f(2,0,0));
        Polygon p = new Polygon();
        p.setVertexIndices(new ArrayList<>(Arrays.asList(0,1,2)));
        model.getPolygons().add(p);
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertEquals(3, model.getNormals().size());
        Vector3f zero = new Vector3f(0,0,0);
        for (Vector3f n : model.getNormals()) {
            Assertions.assertEquals(zero, n);
        }
    }

    @Test
    void testPolygonWithLessThanThreeVertices_Ignored() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(0,0,0));
        model.getVertices().add(new Vector3f(1,0,0));
        Polygon p = new Polygon();
        p.setVertexIndices(new ArrayList<>(Arrays.asList(0,1)));
        model.getPolygons().add(p);
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertEquals(2, model.getNormals().size());
        Vector3f zero = new Vector3f(0,0,0);
        for (Vector3f n : model.getNormals()) {
            Assertions.assertEquals(zero, n);
        }
    }

    @Test
    void testMixedValidAndDegenerateFaces() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(0,0,0));
        model.getVertices().add(new Vector3f(1,0,0));
        model.getVertices().add(new Vector3f(0,1,0));
        model.getVertices().add(new Vector3f(2,0,0));
        Polygon valid = new Polygon();
        valid.setVertexIndices(new ArrayList<>(Arrays.asList(0,1,2)));
        Polygon degenerate = new Polygon();
        degenerate.setVertexIndices(new ArrayList<>(Arrays.asList(1,3,1)));
        model.getPolygons().add(valid);
        model.getPolygons().add(degenerate);
        NormalCalculator.computeVertexNormals(model);
        Assertions.assertEquals(4, model.getNormals().size());
        Assertions.assertEquals(new Vector3f(0,0,1), model.getNormals().get(0));
        Assertions.assertEquals(new Vector3f(0,0,1), model.getNormals().get(1));
        Assertions.assertEquals(new Vector3f(0,0,1), model.getNormals().get(2));
        Assertions.assertEquals(new Vector3f(0,0,0), model.getNormals().get(3));
    }
}

