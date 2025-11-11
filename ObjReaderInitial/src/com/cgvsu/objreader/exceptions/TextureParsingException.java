package com.cgvsu.objreader.exceptions;

public class TextureParsingException extends ObjReaderException {
    
    public TextureParsingException(String message, int lineNumber) {
        super("Texture coordinate parsing error: " + message, lineNumber);
    }

    public TextureParsingException(String message, int lineNumber, Throwable cause) {
        super("Texture coordinate parsing error: " + message, lineNumber, cause);
    }
}