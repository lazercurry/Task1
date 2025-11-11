package com.cgvsu.objreader.exceptions;
public class FaceParsingException extends ObjReaderException {
    
    public FaceParsingException(String message, int lineNumber) {
        super("Face parsing error: " + message, lineNumber);
    }
    
    public FaceParsingException(String message, int lineNumber, Throwable cause) {
        super("Face parsing error: " + message, lineNumber, cause);
    }
}