package com.cgvsu.objreader.exceptions;
public class ObjReaderException extends RuntimeException {
    
    private final int lineNumber;
    private final String context;

    public ObjReaderException(String message, int lineNumber) {
        super(formatMessage(message, lineNumber, null));
        this.lineNumber = lineNumber;
        this.context = null;
    }
    
    public ObjReaderException(String message, int lineNumber, String context) {
        super(formatMessage(message, lineNumber, context));
        this.lineNumber = lineNumber;
        this.context = context;
    }
    
    public ObjReaderException(String message, int lineNumber, Throwable cause) {
        super(formatMessage(message, lineNumber, null), cause);
        this.lineNumber = lineNumber;
        this.context = null;
    }
    
    private static String formatMessage(String message, int lineNumber, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error parsing OBJ file on line ").append(lineNumber).append(": ").append(message);
        if (context != null && !context.trim().isEmpty()) {
            sb.append(" (Context: ").append(context.trim()).append(")");
        }
        return sb.toString();
    }
}