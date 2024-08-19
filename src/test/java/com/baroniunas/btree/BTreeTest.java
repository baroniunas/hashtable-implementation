package com.baroniunas.btree;

import com.baroniunas.collections.btree.BTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BTreeTest {

    private BTree<String, Integer> bTree;

    @BeforeEach
    void setUp() {
        bTree = new BTree<>();
    }

    @Test
    void shouldBeAbleToPutElement() {
        bTree.put("key1", 10);
        assertEquals(10, bTree.getValue("key1"));

        bTree.put("key2", 20);
        bTree.put("key3", 30);
        assertEquals(20, bTree.getValue("key2"));
        assertEquals(30, bTree.getValue("key3"));
    }

    @Test
    void deletedBTreeValueShouldBeNull() {
        bTree.put("key1", 10);
        bTree.put("key2", 20);
        bTree.put("key3", 30);

        bTree.remove("key2");
        assertNull(bTree.getValue("key2"));

        assertEquals(Integer.valueOf(10), bTree.getValue("key1"));
        assertEquals(Integer.valueOf(30), bTree.getValue("key3"));

        bTree.remove("key1");
        bTree.remove("key3");
        assertNull(bTree.getValue("key1"));
        assertNull(bTree.getValue("key3"));
    }
}
