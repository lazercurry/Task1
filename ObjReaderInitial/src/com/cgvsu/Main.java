package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.model.NormalCalculator; // исправлено
import com.cgvsu.objreader.ObjReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("ObjReaderInitial/src/example/FinalBaseMesh.obj");
        String fileContent = Files.readString(fileName);

        System.out.println("Loading model ...");
        Model model = ObjReader.read(fileContent);

        NormalCalculator.computeVertexNormals(model);

        System.out.println("Vertices: " + model.getVertices().size());
        System.out.println("Texture vertices: " + model.getTextureVertices().size());
        System.out.println("Normals (computed): " + model.getNormals().size());
        System.out.println("Polygons: " + model.getPolygons().size());
    }
}