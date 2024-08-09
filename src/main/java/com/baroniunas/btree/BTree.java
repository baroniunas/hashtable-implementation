package com.baroniunas.btree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BTree<K extends Comparable<K>, V> {
    private Node<K, V> root;

    private static final Logger logger = LogManager.getLogger(BTree.class);

    public BTree() {
        root = new Node<>();
        logger.info("BTree created. Root is {}", root);
    }

    public void insert(K key, V value) {
        logger.debug("Inserting key {} with {} into BTree", key, value);
        Node<K, V> rootNode = root;
        if (rootNode.numberOfKeys == Node.MIN_DEGREE * 2 - 1) {
            logger.warn("Root node {} is full creating new node and splitting..", rootNode.toString());
            Node<K, V> newRoot = new Node<>();
            root = newRoot;
            newRoot.leaf = false;
            newRoot.numberOfKeys = 0;
            newRoot.children[0] = rootNode;
            newRoot.splitChild(0, rootNode);
            newRoot.insertNonFull(new Entry<>(key, value));
        } else {
            rootNode.insertNonFull(new Entry<>(key, value));
        }
    }

    public V search(K key) {
        Entry<K, V> entry = root.search(key);
        return entry != null ? entry.value : null;
    }

    public void traverse() {
        if (root != null) {
            root.traverse();
        }
    }
}