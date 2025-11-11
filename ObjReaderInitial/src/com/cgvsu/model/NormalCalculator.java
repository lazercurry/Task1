package com.cgvsu.model;

import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

public final class NormalCalculator {

    private NormalCalculator() {
        throw new AssertionError("Utility class");
    }

    public static void computeVertexNormals(Model model) {
        if (model.getVertices().isEmpty() || model.getPolygons().isEmpty()) {
            model.setNormals(new ArrayList<>());
            return;
        }

        int vertexCount = model.getVertices().size();
        Vector3f[] vertexNormals = new Vector3f[vertexCount];
        int[] normalCounts = new int[vertexCount];

        Arrays.fill(vertexNormals, new Vector3f(0, 0, 0));

        for (Polygon polygon : model.getPolygons()) {
            Vector3f faceNormal = computeFaceNormal(polygon, model);
            if (faceNormal == null) continue;

            for (int vertexIndex : polygon.getVertexIndices()) {
                vertexNormals[vertexIndex] = add(vertexNormals[vertexIndex], faceNormal);
                normalCounts[vertexIndex]++;
            }
        }

        ArrayList<Vector3f> result = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            if (normalCounts[i] > 0) {
                Vector3f avg = scale(vertexNormals[i], 1.0f / normalCounts[i]);
                avg = normalize(avg);
                result.add(avg);
            } else {

                result.add(new Vector3f(0, 0, 0));
            }
        }

        model.setNormals(result);
    }

    private static Vector3f computeFaceNormal(Polygon polygon, Model model) {
        ArrayList<Integer> indices = polygon.getVertexIndices();
        if (indices.size() < 3) return null;

        Vector3f v0 = model.getVertices().get(indices.get(0));
        Vector3f v1 = model.getVertices().get(indices.get(1));
        Vector3f v2 = model.getVertices().get(indices.get(2));

        Vector3f edge1 = subtract(v1, v0);
        Vector3f edge2 = subtract(v2, v0);
        Vector3f normal = cross(edge1, edge2);

        float length = length(normal);
        if (length < 1e-12f) return null;

        return scale(normal, 1.0f / length);
    }


    private static Vector3f add(Vector3f a, Vector3f b) {
        return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static Vector3f subtract(Vector3f a, Vector3f b) {
        return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private static Vector3f scale(Vector3f v, float s) {
        return new Vector3f(v.x * s, v.y * s, v.z * s);
    }

    private static Vector3f cross(Vector3f a, Vector3f b) {
        return new Vector3f(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    private static float length(Vector3f v) {
        return (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    private static Vector3f normalize(Vector3f v) {
        float len = length(v);
        if (len < 1e-12f) return new Vector3f(0, 0, 0);
        return scale(v, 1.0f / len);
    }
}