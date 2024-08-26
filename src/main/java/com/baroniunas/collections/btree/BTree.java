package com.baroniunas.collections.btree;

import com.baroniunas.collections.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BTree<K extends Comparable<K>, V> implements Collections<K,V> {
    private Node<K, V> node;
    private int size = 0;

    private static final Logger logger = LogManager.getLogger(BTree.class);

    public BTree() {
        node = new Node<>();
        logger.info("BTree created.");
    }

    @Override
    public void put(K key, V value) {
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
            size++;
        } else {
            rootNode.insertNonFull(new Entry<>(key, value));
            size++;
        }
    }

    @Override
    public V remove(K key) {
        if (node == null) {
            logger.warn("Tree is empty, nothing to remove.");
            return null;
        }
        logger.info("Removing key {} from BTree", key);
        node.delete(key);
        size--;
        if (node.numberOfKeys == 0 && !node.leaf) {
            node = node.children[0];
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public V getValue(K key) {
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