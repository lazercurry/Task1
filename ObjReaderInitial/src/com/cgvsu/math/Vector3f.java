package com.cgvsu.math;

public class Vector3f {
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f normalize() {
        float length = (float) Math.sqrt(x * x + y * y + z * z);
        if (length < 1e-12f) return new Vector3f(0, 0, 0);
        return new Vector3f(x / length, y / length, z / length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3f v)) return false;
        final float eps = 1e-7f;
        return Math.abs(x - v.x) < eps && Math.abs(y - v.y) < eps && Math.abs(z - v.z) < eps;
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 31 * h + Float.floatToIntBits(x);
        h = 31 * h + Float.floatToIntBits(y);
        h = 31 * h + Float.floatToIntBits(z);
        return h;
    }

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f, %.4f)", x, y, z);
    }
}