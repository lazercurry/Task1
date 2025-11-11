package com.cgvsu.objreader.utils;

import com.cgvsu.objreader.exceptions.InvalidIndexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IndexUtilsTest {

    @Test
    public void testConvertObjIndex_PositiveValid() {
        Assertions.assertEquals(0, IndexUtils.convertObjIndex(1, 5, "vertex", 1));
        Assertions.assertEquals(1, IndexUtils.convertObjIndex(2, 5, "vertex", 1));
        Assertions.assertEquals(4, IndexUtils.convertObjIndex(5, 5, "vertex", 1));
    }

    @Test
    public void testConvertObjIndex_PositiveOutOfBounds() {
        Assertions.assertThrows(InvalidIndexException.class, () -> {
            IndexUtils.convertObjIndex(6, 5, "vertex", 1);
        });
    }
    

    @Test
    public void testConvertObjIndex_NegativeValid() {
        Assertions.assertEquals(4, IndexUtils.convertObjIndex(-1, 5, "vertex", 1));
        Assertions.assertEquals(3, IndexUtils.convertObjIndex(-2, 5, "vertex", 1));
        Assertions.assertEquals(0, IndexUtils.convertObjIndex(-5, 5, "vertex", 1));
    }
    
    @Test
    public void testConvertObjIndex_NegativeOutOfBounds() {
        Assertions.assertThrows(InvalidIndexException.class, () -> {
            IndexUtils.convertObjIndex(-6, 5, "vertex", 1);
        });
    }
    
    @Test
    public void testConvertObjIndex_ZeroIndex() {
        Assertions.assertThrows(InvalidIndexException.class, () -> {
            IndexUtils.convertObjIndex(0, 5, "vertex", 1);
        });
    }
    
    @Test
    public void testConvertObjIndex_EmptyArray() {
        Assertions.assertThrows(InvalidIndexException.class, () -> {
            IndexUtils.convertObjIndex(1, 0, "vertex", 1);
        });
    }
    
    @Test
    public void testIsValidInteger_Valid() {
        Assertions.assertTrue(IndexUtils.isValidInteger("123"));
        Assertions.assertTrue(IndexUtils.isValidInteger("-456"));
        Assertions.assertTrue(IndexUtils.isValidInteger("0"));
        Assertions.assertTrue(IndexUtils.isValidInteger("  789  "));
    }
    
    @Test
    public void testIsValidInteger_Invalid() {
        Assertions.assertFalse(IndexUtils.isValidInteger(""));
        Assertions.assertFalse(IndexUtils.isValidInteger("   "));
        Assertions.assertFalse(IndexUtils.isValidInteger(null));
        Assertions.assertFalse(IndexUtils.isValidInteger("12.5"));
        Assertions.assertFalse(IndexUtils.isValidInteger("abc"));
        Assertions.assertFalse(IndexUtils.isValidInteger("-"));
        Assertions.assertFalse(IndexUtils.isValidInteger("+"));
    }
    
    @Test
    public void testIsValidFloat_Valid() {
        Assertions.assertTrue(IndexUtils.isValidFloat("123.45"));
        Assertions.assertTrue(IndexUtils.isValidFloat("-456.78"));
        Assertions.assertTrue(IndexUtils.isValidFloat("0.0"));
        Assertions.assertTrue(IndexUtils.isValidFloat("123"));
        Assertions.assertTrue(IndexUtils.isValidFloat("  789.12  "));
        Assertions.assertTrue(IndexUtils.isValidFloat(".5"));
        Assertions.assertTrue(IndexUtils.isValidFloat("5."));
    }
    
    @Test
    public void testIsValidFloat_Invalid() {
        Assertions.assertFalse(IndexUtils.isValidFloat(""));
        Assertions.assertFalse(IndexUtils.isValidFloat("   "));
        Assertions.assertFalse(IndexUtils.isValidFloat(null));
        Assertions.assertFalse(IndexUtils.isValidFloat("abc"));
        Assertions.assertFalse(IndexUtils.isValidFloat("-"));
        Assertions.assertFalse(IndexUtils.isValidFloat("+"));
        Assertions.assertFalse(IndexUtils.isValidFloat("."));
        Assertions.assertFalse(IndexUtils.isValidFloat("..5"));
    }
}