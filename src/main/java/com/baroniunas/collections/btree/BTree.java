package com.baroniunas.btree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BTree<K extends Comparable<K>, V> {
    private Node<K, V> node;

    private static final Logger logger = LogManager.getLogger(BTree.class);

    public BTree() {
        node = new Node<>();
        logger.info("BTree created.");
    }

    public void insert(K key, V value) {
        logger.debug("Inserting key {} with value {} into BTree", key, value);
        Node<K, V> rootNode = node;
        if (rootNode.numberOfKeys == Node.MIN_DEGREE * 2 - 1) {
            logger.warn("Node {} is full creating new node and splitting..", rootNode.toString());
            Node<K, V> newRoot = new Node<>();
            node = newRoot;
            newRoot.leaf = false;
            newRoot.numberOfKeys = 0;
            newRoot.children[0] = rootNode;
            newRoot.splitChild(0, rootNode);
            newRoot.insertNonFull(new Entry<>(key, value));
        } else {
            rootNode.insertNonFull(new Entry<>(key, value));
        }
    }

    public void delete(K key) {
        if (node == null) {
            logger.warn("Tree is empty, nothing to delete.");
            return;
        }
        node.delete(key);

        if (node.numberOfKeys == 0 && !node.leaf) {
            node = node.children[0];
        }
    }

    public V search(K key) {
        logger.info("Searching key <{}> in BTree..", key);
        Entry<K, V> entry = node.search(key);
        if (entry != null) {
            logger.info("Found key <{}> with value -{} in BTree.", key, entry.value);
            return entry.value;
        }
        logger.warn("Provided key <{}> not found in BTree.", key);
        return null;
    }

    public void traverse() {
        if (node != null) {
            node.traverse();
        }
    }

    @Override
    public String toString() {
        return node.toString();
    }
}