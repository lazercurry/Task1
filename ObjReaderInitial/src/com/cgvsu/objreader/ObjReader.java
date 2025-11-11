package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.exceptions.*;
import com.cgvsu.objreader.utils.IndexUtils;
import com.cgvsu.objreader.validation.ModelValidator;

import java.util.ArrayList;
import java.util.Scanner;

public class ObjReader {

	private static final String OBJ_VERTEX_TOKEN = "v";
	private static final String OBJ_TEXTURE_TOKEN = "vt";
	private static final String OBJ_NORMAL_TOKEN = "vn"; // добавлено
	private static final String OBJ_FACE_TOKEN = "f";

	private static final int MAX_VERTICES = 10_000_000;
	private static final int MAX_TEXTURE_COORDS = 10_000_000;
	private static final int MAX_NORMALS = 10_000_000;
	private static final int MAX_FACES = 10_000_000;

	public static Model read(String fileContent) {
		if (fileContent == null) {
			throw new IllegalArgumentException("File content cannot be null");
		}
		if (fileContent.trim().isEmpty()) {
			throw new IllegalArgumentException("File content cannot be empty");
		}

		Model result = new Model();
		int lineNumber = 0;

		try (Scanner scanner = new Scanner(fileContent)) {
			while (scanner.hasNextLine()) {
				lineNumber++;
				String line = scanner.nextLine();

				if (isEmptyOrComment(line)) {
					continue;
				}

				ArrayList<String> tokens = parseLine(line, lineNumber);
				if (tokens.isEmpty()) {
					continue;
				}

				String command = tokens.get(0).toLowerCase();
				ArrayList<String> arguments = new ArrayList<>(tokens.subList(1, tokens.size()));

				processCommand(command, arguments, result, lineNumber);
				checkModelLimits(result, lineNumber);
			}
		} catch (ObjReaderException e) {
			throw e;
		} catch (Exception e) {
			throw new ObjReaderException("Unexpected error during parsing", lineNumber, e);
		}

		ModelValidator.validateModel(result);
		return result;
	}

	private static boolean isEmptyOrComment(String line) {
		String trimmed = line.trim();
		return trimmed.isEmpty() || trimmed.startsWith("#");
	}

	private static ArrayList<String> parseLine(String line, int lineNumber) {
		String trimmed = line.trim();
		if (trimmed.isEmpty()) {
			return new ArrayList<>();
		}

		String[] parts = trimmed.split("\\s+");
		ArrayList<String> tokens = new ArrayList<>();

		for (String part : parts) {
			if (!part.isEmpty()) {
				tokens.add(part);
			}
		}

		return tokens;
	}

	private static void processCommand(String command, ArrayList<String> arguments,
									   Model model, int lineNumber) {
		switch (command) {
			case OBJ_VERTEX_TOKEN -> model.getVertices().add(parseVertex(arguments, lineNumber));
			case OBJ_TEXTURE_TOKEN -> model.getTextureVertices().add(parseTextureVertex(arguments, lineNumber));
			case OBJ_NORMAL_TOKEN -> model.getNormals().add(parseNormal(arguments, lineNumber));
			case OBJ_FACE_TOKEN -> model.getPolygons().add(parseFace(arguments, model, lineNumber));
			default -> { }
		}
	}

	private static void checkModelLimits(Model model, int lineNumber) {
		if (model.getVertices().size() > MAX_VERTICES) {
			throw new ModelValidationException(
					String.format("Too many vertices: %d (maximum: %d)", model.getVertices().size(), MAX_VERTICES));
		}
		if (model.getTextureVertices().size() > MAX_TEXTURE_COORDS) {
			throw new ModelValidationException(
					String.format("Too many texture coordinates: %d (maximum: %d)",
							model.getTextureVertices().size(), MAX_TEXTURE_COORDS));
		}

		if (model.getPolygons().size() > MAX_FACES) {
			throw new ModelValidationException(
					String.format("Too many faces: %d (maximum: %d)", model.getPolygons().size(), MAX_FACES));
		}
	}

	protected static Vector3f parseVertex(final ArrayList<String> arguments, int lineNumber) {
		if (arguments.size() < 3) {
			throw new VertexParsingException(
					String.format("Expected 3 coordinates, got %d", arguments.size()), lineNumber);
		}

		if (arguments.size() > 4) {
			throw new VertexParsingException(
					String.format("Too many coordinates: expected 3-4, got %d", arguments.size()), lineNumber);
		}

		try {
			for (int i = 0; i < 3; i++) {
				if (!IndexUtils.isValidFloat(arguments.get(i))) {
					throw new VertexParsingException(
							String.format("Invalid coordinate value '%s' at position %d", arguments.get(i), i + 1),
							lineNumber);
				}
			}

			float x = Float.parseFloat(arguments.get(0));
			float y = Float.parseFloat(arguments.get(1));
			float z = Float.parseFloat(arguments.get(2));

			if (!Float.isFinite(x) || !Float.isFinite(y) || !Float.isFinite(z)) {
				throw new VertexParsingException("Coordinates must be finite numbers", lineNumber);
			}

			if (arguments.size() == 4) {
				if (!IndexUtils.isValidFloat(arguments.get(3))) {
					throw new VertexParsingException(
							String.format("Invalid w coordinate '%s'", arguments.get(3)), lineNumber);
				}
				float w = Float.parseFloat(arguments.get(3));
				if (Math.abs(w) < 1e-10f) {
					throw new VertexParsingException("W coordinate cannot be zero", lineNumber);
				}
				x /= w;
				y /= w;
				z /= w;
			}

			return new Vector3f(x, y, z);

		} catch (NumberFormatException e) {
			throw new VertexParsingException("Failed to parse coordinate values", lineNumber, e);
		}
	}

	protected static Vector2f parseTextureVertex(final ArrayList<String> arguments, int lineNumber) {
		if (arguments.size() < 1) {
			throw new TextureParsingException("At least U coordinate is required", lineNumber);
		}

		if (arguments.size() > 3) {
			throw new TextureParsingException(
					String.format("Too many coordinates: expected 1-3, got %d", arguments.size()), lineNumber);
		}

		try {
			if (!IndexUtils.isValidFloat(arguments.get(0))) {
				throw new TextureParsingException(
						String.format("Invalid U coordinate '%s'", arguments.get(0)), lineNumber);
			}
			float u = Float.parseFloat(arguments.get(0));

			float v = 0.0f;
			if (arguments.size() >= 2) {
				if (!IndexUtils.isValidFloat(arguments.get(1))) {
					throw new TextureParsingException(
							String.format("Invalid V coordinate '%s'", arguments.get(1)), lineNumber);
				}
				v = Float.parseFloat(arguments.get(1));
			}

			if (arguments.size() == 3) {
				if (!IndexUtils.isValidFloat(arguments.get(2))) {
					throw new TextureParsingException(
							String.format("Invalid W coordinate '%s'", arguments.get(2)), lineNumber);
				}
			}

			if (!Float.isFinite(u) || !Float.isFinite(v)) {
				throw new TextureParsingException("Texture coordinates must be finite numbers", lineNumber);
			}

			return new Vector2f(u, v);

		} catch (NumberFormatException e) {
			throw new TextureParsingException("Failed to parse texture coordinate values", lineNumber, e);
		}
	}


	public static Vector3f parseNormal(final ArrayList<String> arguments, int lineNumber) {
		if (arguments.size() != 3) {
			throw new NormalParsingException(
					String.format("Normal must have exactly 3 coordinates, got %d", arguments.size()), lineNumber);
		}
		try {
			for (int i = 0; i < 3; i++) {
				if (!IndexUtils.isValidFloat(arguments.get(i))) {
					throw new NormalParsingException(
							String.format("Invalid normal component '%s' at position %d", arguments.get(i), i + 1),
							lineNumber);
				}
			}
			float x = Float.parseFloat(arguments.get(0));
			float y = Float.parseFloat(arguments.get(1));
			float z = Float.parseFloat(arguments.get(2));
			if (!Float.isFinite(x) || !Float.isFinite(y) || !Float.isFinite(z)) {
				throw new NormalParsingException("Normal components must be finite numbers", lineNumber);
			}
			float len2 = x * x + y * y + z * z;
			if (len2 < 1e-20f) {
				throw new NormalParsingException("Normal vector length cannot be zero", lineNumber);
			}
			return new Vector3f(x, y, z);
		} catch (NumberFormatException e) {
			throw new NormalParsingException("Failed to parse normal components", lineNumber, e);
		}
	}

	protected static Polygon parseFace(final ArrayList<String> arguments, Model model, int lineNumber) {
		if (arguments.isEmpty()) {
			throw new FaceParsingException("Face must have at least one vertex", lineNumber);
		}

		if (arguments.size() < 3) {
			throw new FaceParsingException(
					String.format("Face must have at least 3 vertices, got %d", arguments.size()), lineNumber);
		}

		if (arguments.size() > 100) {
			throw new FaceParsingException(
					String.format("Face has too many vertices: %d (maximum: 100)", arguments.size()), lineNumber);
		}

		ArrayList<Integer> vertexIndices = new ArrayList<>();
		ArrayList<Integer> textureIndices = new ArrayList<>();
		ArrayList<Integer> normalIndices = new ArrayList<>();
		Boolean hasTextures = null;
		Boolean hasNormals = null;

		for (int i = 0; i < arguments.size(); i++) {
			String vertexData = arguments.get(i);
			try {
				FaceVertex faceVertex = parseFaceVertex(vertexData, model, lineNumber);

				vertexIndices.add(faceVertex.vertexIndex);

				if (hasTextures == null) {
					hasTextures = faceVertex.hasTexture;
				} else if (hasTextures != faceVertex.hasTexture) {
					throw new FaceParsingException(
							String.format("Inconsistent texture coordinate usage in face (vertex %d)", i + 1),
							lineNumber);
				}

				if (faceVertex.hasTexture) {
					textureIndices.add(faceVertex.textureIndex);
				}
				if (hasNormals == null) {
					hasNormals = faceVertex.hasNormal;
				} else if (hasNormals != faceVertex.hasNormal) {
					throw new FaceParsingException(
							String.format("Inconsistent normal usage in face (vertex %d)", i + 1),
							lineNumber);
				}
				if (faceVertex.hasNormal) {
					normalIndices.add(faceVertex.normalIndex);
				}

			} catch (Exception e) {
				throw new FaceParsingException(
						String.format("Error parsing vertex %d: %s", i + 1, e.getMessage()),
						lineNumber, e);
			}
		}

		Polygon result = new Polygon();
		result.setVertexIndices(vertexIndices);
		result.setTextureVertexIndices(textureIndices);
		result.setNormalIndices(normalIndices);
		return result;
	}

	private static class FaceVertex {
		final int vertexIndex;
		final boolean hasTexture;
		final int textureIndex;
		final boolean hasNormal;
		final int normalIndex;
		FaceVertex(int vertexIndex, boolean hasTexture, int textureIndex,
				   boolean hasNormal, int normalIndex) {
			this.vertexIndex = vertexIndex;
			this.hasTexture = hasTexture;
			this.textureIndex = textureIndex;
			this.hasNormal = hasNormal;
			this.normalIndex = normalIndex;
		}
	}

	private static FaceVertex parseFaceVertex(String vertexData, Model model, int lineNumber) {
		if (vertexData == null || vertexData.trim().isEmpty()) {
			throw new FaceParsingException("Vertex data cannot be empty", lineNumber);
		}

		String[] components = vertexData.split("/", -1);

		if (components.length == 0 || components.length > 3) {
			throw new FaceParsingException(
					String.format("Invalid vertex format '%s'", vertexData), lineNumber);
		}

		if (components[0].trim().isEmpty()) {
			throw new FaceParsingException("Vertex index cannot be empty", lineNumber);
		}

		int vertexIndex;
		try {
			if (!IndexUtils.isValidInteger(components[0])) {
				throw new FaceParsingException(
						String.format("Invalid vertex index '%s'", components[0]), lineNumber);
			}
			int objVertexIndex = Integer.parseInt(components[0].trim());
			vertexIndex = IndexUtils.convertObjIndex(objVertexIndex, model.getVertices().size(),
					"vertex", lineNumber);
		} catch (NumberFormatException e) {
			throw new FaceParsingException(
					String.format("Cannot parse vertex index '%s'", components[0]), lineNumber, e);
		}

		boolean hasTexture = false;
		int textureIndex = -1;
		if (components.length >= 2 && !components[1].trim().isEmpty()) {
			try {
				if (!IndexUtils.isValidInteger(components[1])) {
					throw new FaceParsingException(
							String.format("Invalid texture index '%s'", components[1]), lineNumber);
				}
				int objTextureIndex = Integer.parseInt(components[1].trim());
				textureIndex = IndexUtils.convertObjIndex(objTextureIndex, model.getTextureVertices().size(),
						"texture", lineNumber);
				hasTexture = true;
			} catch (NumberFormatException e) {
				throw new FaceParsingException(
						String.format("Cannot parse texture index '%s'", components[1]), lineNumber, e);
			}
		}
		boolean hasNormal = false;
		int normalIndex = -1;
		if (components.length == 3 && !components[2].trim().isEmpty()) {
			try {
				if (!IndexUtils.isValidInteger(components[2])) {
					throw new FaceParsingException(
							String.format("Invalid normal index '%s'", components[2]), lineNumber);
				}
				int objNormalIndex = Integer.parseInt(components[2].trim());
				normalIndex = IndexUtils.convertObjIndex(objNormalIndex, model.getNormals().size(),
						"normal", lineNumber);
				hasNormal = true;
			} catch (NumberFormatException e) {
				throw new FaceParsingException(
						String.format("Cannot parse normal index '%s'", components[2]), lineNumber, e);
			}
		}
		return new FaceVertex(vertexIndex, hasTexture, textureIndex, hasNormal, normalIndex);
	}
}