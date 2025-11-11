package com.cgvsu.objreader.exceptions;

public class NormalParsingException extends ObjReaderException {
    
    public NormalParsingException(String message, int lineNumber) {
        super("Normal vector parsing error: " + message, lineNumber);
    }
    
    public NormalParsingException(String message, int lineNumber, Throwable cause) {
        super("Normal vector parsing error: " + message, lineNumber, cause);
    }
}