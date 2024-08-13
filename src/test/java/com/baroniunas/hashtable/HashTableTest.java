package com.baroniunas.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

 /*   @Test
    void hashcode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = HashTable.class.getDeclaredMethod("hash", String.class);
        method.setAccessible(true);
        String res = (String) method.invoke(hashTable, "ABCDF");
    }
*/
    @Test
    void shouldBeAbleToPutAndGetValue() {
        hashTable.put("One", 1);
        hashTable.put("Two", 2);
        hashTable.put("Three", 3);

        assertEquals(1, hashTable.getValue("One"));
        assertEquals(2, hashTable.getValue("Two"));
        assertEquals(3, hashTable.getValue("Three"));
    }


    @Test
    void puttingNullKeyNotAvailable() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hashTable.put(null, 1);
        });
        assertEquals("Key or value cannot be null", exception.getMessage());
    }

    @Test
    void puttingNullValueNotAvailable() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hashTable.put("One", null);
        });
        assertEquals("Key or value cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnNullForNonExistentKey() {
        assertNull(hashTable.getValue("Test"));
    }

    @Test
    void shouldThrowErrorGetValueNullKey() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hashTable.getValue(null);
        });
        assertEquals("Key cannot be null", exception.getMessage());
    }

    @Test
    void shouldBeAbleToRemovePair() {
        hashTable.put("One", 1);
        hashTable.put("Two", 2);
        hashTable.put("Three", 3);

        assertEquals(2, hashTable.removePair("Two"));
        assertNull(hashTable.getValue("Two"));
        assertEquals(2, hashTable.getSize());
    }

    @Test
    void shouldReturnNullRemovingNonExistentKey() {
        hashTable.put("One", 1);
        assertNull(hashTable.removePair("Two"));
    }

    @Test
    void passingSameKeyWithNewValueShouldUpdate(){
        hashTable.put("One", 1);
        hashTable.put("One", 2);
        assertEquals(2, hashTable.getValue("One"));
    }

    @Test
    void shouldNotLetToRemoveNullKey() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            hashTable.removePair(null);
        });
        assertEquals("Key cannot be null", exception.getMessage());
    }

    @Test
    void whenPuttingNewElementsShouldResize() {
        hashTable.put("One", 1);
        hashTable.put("Two", 2);
        hashTable.put("Three", 3);
        assertEquals(3, hashTable.getCurrentCapacity());
        hashTable.put("Four", 4);
        assertEquals(6, hashTable.getCurrentCapacity());
    }

    @Test
    void whenRemovingElementsShouldShrink() {
        hashTable.put("One", 1);
        hashTable.put("Two", 2);
        hashTable.put("Three", 3);
        hashTable.put("Four", 4);
        assertEquals(6, hashTable.getCurrentCapacity());
        hashTable.removePair("Four");
        hashTable.removePair("Three");
        hashTable.removePair("Two");
        assertEquals(3, hashTable.getCurrentCapacity());
    }

    @Test
    void transformsToBtreeExceedingSize6() {
        hashTable.put("One", 1);
        hashTable.put("Two", 2);
        hashTable.put("Three", 3);
        hashTable.put("Four", 4);
        hashTable.put("Five", 5);
        hashTable.put("Six", 6);
        assertNotNull(hashTable.getBTree());
    }

    @Test

    void afterTransformingToBTreeTableShouldBeNull() {
        hashTable.put("One", 1);
        hashTable.put("Two", 2);
        hashTable.put("Three", 3);
        hashTable.put("Four", 4);
        hashTable.put("Five", 5);
        hashTable.put("Six", 6);
        assertNull(hashTable.getTable());
    }


    @Test
    void testToString() {
        hashTable.put("T", 1);
        hashTable.put("E", 2);
        hashTable.put("S", 3);
        String expected = "{T=1,E=2,S=3}";
        assertEquals(expected, hashTable.toString());
    }
}
