package com.baroniunas.btree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    private Node<String, Integer> node;

    @BeforeEach
    void setUp() {
        node = new Node<>();
    }

    @Test
    void testInsertNonFull_LeafNode() {
        Entry<String, Integer> entry1 = new Entry<>("apple", 10);
        Entry<String, Integer> entry2 = new Entry<>("banana", 20);

        node.insertNonFull(entry1);
        node.insertNonFull(entry2);

        assertEquals(2, node.numberOfKeys);
        assertEquals("apple", node.entries[0].key);
        assertEquals("banana", node.entries[1].key);
    }

    @Test
    void testInsertNonFull_NonLeafNode() {
        Entry<String, Integer> entry1 = new Entry<>("apple", 10);
        Entry<String, Integer> entry2 = new Entry<>("banana", 20);
        Entry<String, Integer> entry3 = new Entry<>("cherry", 30);

        node.insertNonFull(entry1);
        node.insertNonFull(entry2);

        Node<String, Integer> child = new Node<>();
        child.insertNonFull(entry3);
        node.children[0] = child;
        node.leaf = false;

        assertEquals(2, node.numberOfKeys);
        assertEquals("apple", node.entries[0].key);
        assertEquals("banana", node.entries[1].key);
        assertEquals(1, node.children[0].numberOfKeys);
        assertEquals("cherry", node.children[0].entries[0].key);
    }

    @Test
    void testSplitChild() {
        Entry<String, Integer> entry1 = new Entry<>("apple", 10);
        Entry<String, Integer> entry2 = new Entry<>("banana", 20);
        Entry<String, Integer> entry3 = new Entry<>("cherry", 30);

        Node<String, Integer> fullChild = new Node<>();
        fullChild.insertNonFull(entry1);
        fullChild.insertNonFull(entry2);
        fullChild.insertNonFull(entry3);

        node.children[0] = fullChild;
        node.splitChild(0, fullChild);

        assertEquals(1, node.numberOfKeys);
        assertEquals("banana", node.entries[0].key);
        assertEquals(1, node.children[0].numberOfKeys);
        assertEquals("apple", node.children[0].entries[0].key);
        assertEquals(1, node.children[1].numberOfKeys);
        assertEquals("cherry", node.children[1].entries[0].key);
    }

    @Test
    void testDelete_LeafNode() {
        Entry<String, Integer> entry1 = new Entry<>("apple", 10);
        Entry<String, Integer> entry2 = new Entry<>("banana", 20);

        node.insertNonFull(entry1);
        node.insertNonFull(entry2);

        node.delete("apple");
        assertEquals(1, node.numberOfKeys);
        assertEquals("banana", node.entries[0].key);

        node.delete("banana");
        assertEquals(0, node.numberOfKeys);
    }

    @Test
    void testDelete_NonLeafNode() {
        Entry<String, Integer> entry1 = new Entry<>("apple", 10);
        Entry<String, Integer> entry2 = new Entry<>("banana", 20);
        Entry<String, Integer> entry3 = new Entry<>("cherry", 30);

        node.insertNonFull(entry1);
        node.insertNonFull(entry2);
        node.insertNonFull(entry3);

        Node<String, Integer> child = new Node<>();
        child.insertNonFull(new Entry<>("apricot", 5));
        node.children[0] = child;
        node.leaf = false;

        node.delete("banana");
        assertEquals(2, node.numberOfKeys);
        assertEquals("apple", node.entries[0].key);
        assertEquals("cherry", node.entries[1].key);
    }

    @Test
    void testSearch_Found() {
        Entry<String, Integer> entry = new Entry<>("apple", 10);
        node.insertNonFull(entry);

        Entry<String, Integer> found = node.search("apple");
        assertNotNull(found);
        assertEquals(10, found.value);
    }

    @Test
    void testSearch_NotFound() {
        Entry<String, Integer> entry = new Entry<>("apple", 10);
        node.insertNonFull(entry);

        Entry<String, Integer> found = node.search("banana");
        assertNull(found);
    }

    @Test
    void testTraverse() {
        Entry<String, Integer> entry1 = new Entry<>("apple", 10);
        Entry<String, Integer> entry2 = new Entry<>("banana", 20);

        node.insertNonFull(entry1);
        node.insertNonFull(entry2);

        // Should print "apple: 10" and "banana: 20"
        node.traverse();
    }
}
