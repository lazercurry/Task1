package com.cgvsu.objreader.validation;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.exceptions.ModelValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelValidatorTest {


    @Test
    public void testValidateModel_Null() {
        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(null);
        });
    }

    @Test
    public void testValidateModel_Empty() {
        Model model = new Model();
        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }

    @Test
    public void testValidateModel_OnlyVertices() {
        Model model = new Model();
        model.getVertices().add(new Vector3f(1, 2, 3));

        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }

    @Test
    public void testValidateModel_OnlyPolygons() {
        Model model = new Model();
        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.getPolygons().add(polygon);

        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }

    @Test
    public void testValidateModel_Valid() {
        Model model = new Model();
        
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.getPolygons().add(polygon);

        Assertions.assertDoesNotThrow(() -> {
            ModelValidator.validateModel(model);
        });
    }

    @Test
    public void testValidateModel_InvalidVertexIndex() {
        Model model = new Model();
        
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.getPolygons().add(polygon);

        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }

    @Test
    public void testValidateModel_InvalidTextureIndex() {
        Model model = new Model();
        
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        model.getTextureVertices().add(new Vector2f(0, 0));
        model.getTextureVertices().add(new Vector2f(1, 0));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.getPolygons().add(polygon);

        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }


    @Test
    public void testValidateModel_TextureWithoutVertices() {
        Model model = new Model();
        
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.getPolygons().add(polygon);

        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }


    @Test
    public void testValidateModel_InconsistentTextures() {
        Model model = new Model();
        
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));
        model.getVertices().add(new Vector3f(1, 1, 0));

        model.getTextureVertices().add(new Vector2f(0, 0));
        model.getTextureVertices().add(new Vector2f(1, 0));
        model.getTextureVertices().add(new Vector2f(0, 1));

        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        polygon1.setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2)));
        model.getPolygons().add(polygon1);

        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 3)));
        model.getPolygons().add(polygon2);

        Assertions.assertThrows(ModelValidationException.class, () -> {
            ModelValidator.validateModel(model);
        });
    }
}