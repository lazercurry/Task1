package com.cgvsu.objreader.exceptions;

public class VertexParsingException extends ObjReaderException {
    
    public VertexParsingException(String message, int lineNumber) {
        super("Vertex parsing error: " + message, lineNumber);
    }
    
    public VertexParsingException(String message, int lineNumber, Throwable cause) {
        super("Vertex parsing error: " + message, lineNumber, cause);
    }
}