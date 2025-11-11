package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.exceptions.ObjReaderException;
import com.cgvsu.objreader.exceptions.VertexParsingException;
import com.cgvsu.objreader.exceptions.TextureParsingException;
import com.cgvsu.objreader.exceptions.NormalParsingException;
import com.cgvsu.objreader.exceptions.FaceParsingException;
import com.cgvsu.objreader.exceptions.ModelValidationException;
import com.cgvsu.objreader.exceptions.InvalidIndexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
class ObjReaderTest {


    @Test
    public void testParseVertex_Valid() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("1.01", "1.02", "1.03"));
        final Vector3f result = ObjReader.parseVertex(args, 5);
        final Vector3f expected = new Vector3f(1.01f, 1.02f, 1.03f);
        Assertions.assertTrue(result.equals(expected));
    }

    @Test
    public void testParseVertex_InvalidFloat() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("ab", "o", "ba"));
        Assertions.assertThrows(VertexParsingException.class, () -> {
            ObjReader.parseVertex(args, 10);
        });
    }

    @Test
    public void testParseVertex_TooFewArguments() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("1.0", "2.0"));
        Assertions.assertThrows(VertexParsingException.class, () -> {
            ObjReader.parseVertex(args, 10);
        });
    }

    @Test
    public void testParseVertex_TooManyArguments() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("1.0", "2.0", "3.0", "4.0", "5.0"));
        Assertions.assertThrows(VertexParsingException.class, () -> {
            ObjReader.parseVertex(args, 10);
        });
    }

    @Test
    public void testParseVertex_HomogeneousCoordinates() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("2.0", "4.0", "6.0", "2.0"));
        final Vector3f result = ObjReader.parseVertex(args, 5);
        final Vector3f expected = new Vector3f(1.0f, 2.0f, 3.0f);
        Assertions.assertTrue(result.equals(expected));
    }

    @Test
    public void testParseVertex_InfiniteValues() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("Infinity", "2.0", "3.0"));
        Assertions.assertThrows(VertexParsingException.class, () -> {
            ObjReader.parseVertex(args, 10);
        });
    }

    @Test
    public void testParseVertex_NaNValues() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("1.0", "NaN", "3.0"));
        Assertions.assertThrows(VertexParsingException.class, () -> {
            ObjReader.parseVertex(args, 10);
        });
    }

    @Test
    public void testParseTextureVertex_Valid2D() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("0.5", "0.7"));
        final Vector2f result = ObjReader.parseTextureVertex(args, 5);
        final Vector2f expected = new Vector2f(0.5f, 0.7f);
        Assertions.assertTrue(result.equals(expected));
    }

    @Test
    public void testParseTextureVertex_ValidUOnly() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("0.5"));
        final Vector2f result = ObjReader.parseTextureVertex(args, 5);
        final Vector2f expected = new Vector2f(0.5f, 0.0f);
        Assertions.assertTrue(result.equals(expected));
    }

    @Test
    public void testParseTextureVertex_Valid3D() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("0.5", "0.7", "0.1"));
        final Vector2f result = ObjReader.parseTextureVertex(args, 5);
        final Vector2f expected = new Vector2f(0.5f, 0.7f);
        Assertions.assertTrue(result.equals(expected));
    }

    @Test
    public void testParseTextureVertex_NoArguments() {
        final ArrayList<String> args = new ArrayList<>();
        Assertions.assertThrows(TextureParsingException.class, () -> {
            ObjReader.parseTextureVertex(args, 10);
        });
    }

    @Test
    public void testParseNormal_Valid() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("0.0", "1.0", "0.0"));
        final Vector3f result = ObjReader.parseNormal(args, 5);
        final Vector3f expected = new Vector3f(0.0f, 1.0f, 0.0f);
        Assertions.assertTrue(result.equals(expected));
    }

    @Test
    public void testParseNormal_ZeroLength() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("0.0", "0.0", "0.0"));
        Assertions.assertThrows(NormalParsingException.class, () -> {
            ObjReader.parseNormal(args, 10);
        });
    }

    @Test
    public void testParseNormal_WrongArgumentCount() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("0.0", "1.0"));
        Assertions.assertThrows(NormalParsingException.class, () -> {
            ObjReader.parseNormal(args, 10);
        });
    }

    @Test
    public void testReadModel_Simple() {
        String content = """
            v 1.0 2.0 3.0
            v 4.0 5.0 6.0
            v 7.0 8.0 9.0
            f 1 2 3
            """;
        
        Model model = ObjReader.read(content);
        Assertions.assertEquals(3, model.getVertices().size());
        Assertions.assertEquals(1, model.getPolygons().size());
    }

    @Test
    public void testReadModel_WithComments() {
        String content = """
            # This is a comment
            v 1.0 2.0 3.0
            # Another comment
            v 4.0 5.0 6.0
            v 7.0 8.0 9.0
            f 1 2 3
            """;
        
        Model model = ObjReader.read(content);
        Assertions.assertEquals(3, model.getVertices().size());
        Assertions.assertEquals(1, model.getPolygons().size());
    }

    @Test
    public void testReadModel_EmptyContent() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ObjReader.read("");
        });
    }

    @Test
    public void testReadModel_NullContent() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ObjReader.read(null);
        });
    }

    @Test
    public void testReadModel_RelativeIndices() {
        String content = """
            v 1.0 0.0 0.0
            v 0.0 1.0 0.0
            v 0.0 0.0 1.0
            f -3 -2 -1
            """;
        
        Model model = ObjReader.read(content);
        Polygon face = model.getPolygons().get(0);


        Assertions.assertEquals(0, (int) face.getVertexIndices().get(0));
        Assertions.assertEquals(1, (int) face.getVertexIndices().get(1));
        Assertions.assertEquals(2, (int) face.getVertexIndices().get(2));
    }

    @Test
    public void testReadModel_InvalidRelativeIndex() {
        String content = """
            v 1.0 0.0 0.0
            f -5
            """;
        
        Assertions.assertThrows(FaceParsingException.class, () -> {
            ObjReader.read(content);
        });
    }

    @Test
    public void testReadModel_TextureCoordinates() {
        String content = """
            v 1.0 0.0 0.0
            v 0.0 1.0 0.0
            v 0.0 0.0 1.0
            vt 0.0 0.0
            vt 1.0 0.0
            vt 0.5 1.0
            f 1/1 2/2 3/3
            """;
        
        Model model = ObjReader.read(content);
        Assertions.assertEquals(3, model.getVertices().size());
        Assertions.assertEquals(3, model.getTextureVertices().size());

        Polygon face = model.getPolygons().get(0);
        Assertions.assertEquals(3, face.getTextureVertexIndices().size());
    }

    @Test
    public void testReadModel_WithNormals() {
        String content = """
            v 1.0 0.0 0.0
            v 0.0 1.0 0.0
            v 0.0 0.0 1.0
            vn 1.0 0.0 0.0
            vn 0.0 1.0 0.0
            vn 0.0 0.0 1.0
            f 1//1 2//2 3//3
            """;
        
        Model model = ObjReader.read(content);
        Polygon face = model.getPolygons().get(0);
        Assertions.assertEquals(3, face.getNormalIndices().size());
    }

    @Test
    public void testReadModel_InconsistentTextureUsage() {
        String content = """
            v 1.0 0.0 0.0
            v 0.0 1.0 0.0
            v 0.0 0.0 1.0
            vt 0.0 0.0
            f 1/1 2 3
            """;
        
        Assertions.assertThrows(FaceParsingException.class, () -> {
            ObjReader.read(content);
        });
    }

    @Test
    public void testReadModel_NoVertices() {
        String content = """
            f 1 2 3
            """;
        
        Assertions.assertThrows(FaceParsingException.class, () -> {
            ObjReader.read(content);
        });
    }

    @Test
    public void testReadModel_EmptyModel() {
        String content = """
            # Just comments
            """;
        
        Assertions.assertThrows(ModelValidationException.class, () -> {
            ObjReader.read(content);
        });
    }

    @Test
    public void testReadModel_FaceWithTooFewVertices() {
        String content = """
            v 1.0 0.0 0.0
            v 0.0 1.0 0.0
            f 1 2
            """;
        
        Assertions.assertThrows(FaceParsingException.class, () -> {
            ObjReader.read(content);
        });
    }

    @Test
    public void testReadModel_InvalidVertexIndex() {
        String content = """
            v 1.0 0.0 0.0
            v 0.0 1.0 0.0
            v 0.0 0.0 1.0
            f 1 2 5
            """;
        
        Assertions.assertThrows(FaceParsingException.class, () -> {
            ObjReader.read(content);
        });
    }

    @Test
    public void testParseNormal_InvalidFloat() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("a", "0.0", "1.0"));
        Assertions.assertThrows(NormalParsingException.class, () -> ObjReader.parseNormal(args, 15));
    }

    @Test
    public void testParseNormal_InfiniteValues() {
        final ArrayList<String> args = new ArrayList<>(Arrays.asList("Infinity", "0.0", "0.0"));
        Assertions.assertThrows(NormalParsingException.class, () -> ObjReader.parseNormal(args, 16));
    }

    @Test
    public void testReadModel_WithNormalsInvalidIndex() {
        String content = """
            v 0 0 0
            v 1 0 0
            v 0 1 0
            vn 0 0 1
            f 1//1 2//2 3//3
            """;
        Assertions.assertThrows(FaceParsingException.class, () -> ObjReader.read(content));
    }

    @Test
    public void testReadModel_FaceInconsistentNormals() {
        String content = """
            v 0 0 0
            v 1 0 0
            v 0 1 0
            vn 0 0 1
            f 1//1 2 3//1
            """;
        Assertions.assertThrows(FaceParsingException.class, () -> ObjReader.read(content));
    }

    @Test
    public void testReadModel_ModelInconsistentNormalsBetweenPolygons() {
        String content = """
            v 0 0 0
            v 1 0 0
            v 0 1 0
            v 1 1 0
            vn 0 0 1
            vn 0 0 1
            vn 0 0 1
            f 1//1 2//2 3//3
            f 1 2 4
            """;
        Assertions.assertThrows(ModelValidationException.class, () -> ObjReader.read(content));
    }

    @Test
    public void testReadModel_InvalidNormalIndex() {
        String content = """
            v 0 0 0
            v 1 0 0
            v 0 1 0
            vn 0 0 1
            vn 0 1 0
            vn 1 0 0
            f 1//1 2//5 3//3
            """;
        Assertions.assertThrows(FaceParsingException.class, () -> ObjReader.read(content));
    }

    @Test
    public void testReadModel_NonIntegerNormalIndex() {
        String content = """
            v 0 0 0
            v 1 0 0
            v 0 1 0
            vn 0 0 1
            vn 0 1 0
            vn 1 0 0
            f 1//1 2//a 3//3
            """;
        Assertions.assertThrows(FaceParsingException.class, () -> ObjReader.read(content));
    }
}