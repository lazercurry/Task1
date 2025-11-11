package com.cgvsu.objreader.exceptions;
public class InvalidIndexException extends ObjReaderException {
    
    public InvalidIndexException(String indexType, int index, int maxValidIndex, int lineNumber) {
        super(String.format("Invalid %s index %d (valid range: 1-%d or -%d-(-1))", 
                indexType, index, maxValidIndex, maxValidIndex), lineNumber);
    }
    
    public InvalidIndexException(String indexType, int index, int maxValidIndex, int lineNumber, String context) {
        super(String.format("Invalid %s index %d (valid range: 1-%d or -%d-(-1))", 
                indexType, index, maxValidIndex, maxValidIndex), lineNumber, context);
    }
}