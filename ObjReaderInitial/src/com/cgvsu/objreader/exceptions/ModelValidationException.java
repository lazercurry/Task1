package com.cgvsu.objreader.exceptions;

public class ModelValidationException extends ObjReaderException {
    public ModelValidationException(String message) {
        super("Model validation error: " + message, -1);
    }
}