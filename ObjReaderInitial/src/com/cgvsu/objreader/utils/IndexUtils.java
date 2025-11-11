package com.cgvsu.objreader.utils;

import com.cgvsu.objreader.exceptions.InvalidIndexException;
public final class IndexUtils {
    private IndexUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }
    
    public static int convertObjIndex(int objIndex, int arraySize, String indexType, int lineNumber) {
        if (arraySize == 0) {
            throw new InvalidIndexException(indexType, objIndex, 0, lineNumber, 
                "No " + indexType + " elements defined yet");
        }
        
        int zeroBasedIndex;
        
        if (objIndex > 0) {
            zeroBasedIndex = objIndex - 1;
            if (zeroBasedIndex >= arraySize) {
                throw new InvalidIndexException(indexType, objIndex, arraySize, lineNumber);
            }
        } else if (objIndex < 0) {
            zeroBasedIndex = arraySize + objIndex;
            if (zeroBasedIndex < 0) {
                throw new InvalidIndexException(indexType, objIndex, arraySize, lineNumber);
            }
        } else {
            throw new InvalidIndexException(indexType, objIndex, arraySize, lineNumber,
                "Index 0 is not allowed in OBJ files");
        }
        
        return zeroBasedIndex;
    }
    
    public static boolean isValidInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = str.trim();
        if (trimmed.equals("-") || trimmed.equals("+")) {
            return false;
        }
        
        try {
            Integer.parseInt(trimmed);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidFloat(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = str.trim();
        if (trimmed.equals("-") || trimmed.equals("+") || trimmed.equals(".")) {
            return false;
        }
        
        try {
            Float.parseFloat(trimmed);
            return !Float.isNaN(Float.parseFloat(trimmed));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}