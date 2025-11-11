package com.cgvsu.objreader.validation;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.exceptions.ModelValidationException;

import java.util.ArrayList;

public class ModelValidator {
    
    public static void validateModel(Model model) {
        if (model == null) {
            throw new ModelValidationException("Model cannot be null");
        }
        
        validateBasicStructure(model);
        validateConsistency(model);
    }
    
    private static void validateBasicStructure(Model model) {
        if (model.getVertices().isEmpty() && model.getPolygons().isEmpty()) {
            throw new ModelValidationException("Model contains no vertices or polygons (empty model)");
        }
        if (!model.getVertices().isEmpty() && model.getPolygons().isEmpty()) {
            throw new ModelValidationException(
                String.format("Model contains %d vertices but no polygons", model.getVertices().size()));
        }
        if (model.getVertices().isEmpty() && !model.getPolygons().isEmpty()) {
            throw new ModelValidationException(
                String.format("Model contains %d polygons but no vertices", model.getPolygons().size()));
        }
        int vertexCount = model.getVertices().size();
        int textureCount = model.getTextureVertices().size();
        int normalCount = model.getNormals().size();
        for (int i = 0; i < model.getPolygons().size(); i++) {
            Polygon polygon = model.getPolygons().get(i);
            validatePolygonIndices(polygon, i, vertexCount, textureCount, normalCount);
        }
    }
    
    private static void validatePolygonIndices(Polygon polygon, int polygonIndex, 
                                               int vertexCount, int textureCount, int normalCount) {
        ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
        if (vertexIndices.isEmpty()) {
            throw new ModelValidationException(
                String.format("Polygon %d has no vertex indices", polygonIndex));
        }
        
        for (int index : vertexIndices) {
            if (index < 0 || index >= vertexCount) {
                throw new ModelValidationException(
                    String.format("Polygon %d references invalid vertex index %d (valid range: 0-%d)", 
                        polygonIndex, index, vertexCount - 1));
            }
        }
        
        ArrayList<Integer> textureIndices = polygon.getTextureVertexIndices();
        if (!textureIndices.isEmpty()) {
            if (textureCount == 0) {
                throw new ModelValidationException(
                    String.format("Polygon %d references texture coordinates but model has no texture vertices",
                            polygonIndex));
            }
            
            for (int index : textureIndices) {
                if (index < 0 || index >= textureCount) {
                    throw new ModelValidationException(
                        String.format("Polygon %d references invalid texture index %d (valid range: 0-%d)", 
                            polygonIndex, index, textureCount - 1));
                }
            }
        }
        
        ArrayList<Integer> normalIndices = polygon.getNormalIndices();
        if (!normalIndices.isEmpty()) {
            if (normalCount == 0) {
                throw new ModelValidationException(
                    String.format("Polygon %d references normals but model has no normal vectors",
                            polygonIndex));
            }
            
            for (int index : normalIndices) {
                if (index < 0 || index >= normalCount) {
                    throw new ModelValidationException(
                        String.format("Polygon %d references invalid normal index %d (valid range: 0-%d)", 
                            polygonIndex, index, normalCount - 1));
                }
            }
        }
    }
    
    private static void validateConsistency(Model model) {
        boolean hasTextureReferences = false;
        boolean hasNormalReferences = false;
        boolean hasInconsistentTextures = false;
        boolean hasInconsistentNormals = false;
        
        for (Polygon polygon : model.getPolygons()) {
            boolean polygonHasTextures = !polygon.getTextureVertexIndices().isEmpty();
            boolean polygonHasNormals = !polygon.getNormalIndices().isEmpty();
            
            if (polygonHasTextures) {
                hasTextureReferences = true;
            }
            if (polygonHasNormals) {
                hasNormalReferences = true;
            }
            
            if (hasTextureReferences && !polygonHasTextures) {
                hasInconsistentTextures = true;
            }
            
            if (hasNormalReferences && !polygonHasNormals) {
                hasInconsistentNormals = true;
            }
        }
        
        if (hasInconsistentTextures) {
            throw new ModelValidationException(
                "Model has inconsistent texture coordinate usage: some polygons have texture coordinates, others don't. " +
                        "Total polygons: " + model.getPolygons().size());
        }
        
        if (hasInconsistentNormals) {
            throw new ModelValidationException(
                "Model has inconsistent normal vector usage: some polygons have normals, others don't. " +
                        "Total polygons: " + model.getPolygons().size());
        }
    }
}