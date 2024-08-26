package com.baroniunas.hashtable;

import com.baroniunas.collections.Collections;
import com.baroniunas.collections.btree.BTree;
import com.baroniunas.collections.linkedlist.LinkedList;
import com.baroniunas.collections.linkedlist.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashTable<K extends Comparable<K>, V> {
    private static final int INITIAL_CAPACITY = 3;
    private static final float LOAD_FACTOR = 0.65f;
    private static final int TREE_THRESHOLD = 6;
    private int arraySize;
    private Collections<K, V>[] collections;


    private static final Logger logger = LogManager.getLogger(HashTable.class);

    @SuppressWarnings("unchecked")
    public HashTable() {
        collections = new Collections[INITIAL_CAPACITY];
        arraySize = 0;
        logger.info("Hash table initialized, initial capacity: {}", INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        logger.info("Starting to resize table capacity to {}.", newCapacity);
        Collections<K, V>[] oldCollections = collections;
        collections = new Collections[newCapacity];
        arraySize = 0;

        for (Collections<K, V> collection : oldCollections) {
            if (collection != null) {
                if (collection instanceof LinkedList) {
                    Node<K, V> node = ((LinkedList<K, V>) collection).getHead();
                    while (node != null) {
                        int newIndex = hash(node.getKey(), newCapacity);
                        if(collections[newIndex] == null) {
                            collections[newIndex] = new LinkedList<>();
                            arraySize++;
                        }
                        collections[newIndex].put(node.getKey(), node.getValue());
                        node = node.getNext();
                    }
                }
            }
        }
        logger.info("Resizing completed.");
    }

    public void put(K key, V value) {
        int index = hash(key);

        logger.info("Adding new element to the table with key <{}> and value <{}> at index {}.", key, value, index);

        if (collections[index] == null) {
            logger.info("No collection found at index {}. Initializing new LinkedList.", index);
            collections[index] = new LinkedList<>();
            arraySize++;
            logger.info("Collection array size is now {}.", arraySize);
        }

        Collections<K, V> collection = collections[index];

        if ( arraySize >= collections.length * LOAD_FACTOR) {
            resize(collections.length * 2);
        }

        collection.put(key, value);

        if (collection instanceof LinkedList && collection.getSize() == TREE_THRESHOLD) {
            transformToBTree(index);
        }
    }

    private void transformToBTree(int index) {
        logger.info("Transforming to btree index {}.", index);
        LinkedList<K, V> linkedList = (LinkedList<K, V>) collections[index];
        BTree<K, V> bTree = new BTree<>();

        Node<K, V> node = linkedList.getHead();
        while (node != null) {
            bTree.put(node.getKey(), node.getValue());
            node = node.getNext();
        }

        collections[index] = bTree;
        logger.info("Transformation successful.");
    }

    public V getValue(K key) {
        int index = hash(key);
        return collections[index].getValue(key);
    }

    public Collections<K, V> getCollectionAtIndex(int index) {
        if (index < 0 || index >= collections.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return collections[index];
    }

    public V removePair(K key) {
        int index = hash(key);

        if (collections[index] == null)
            return null;

        V value = collections[index].remove(key);
        if (value != null && collections[index].getSize() == 0) {
            collections[index] = null;
            arraySize--;
        }

        return value;
    }


 /*   @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Entity<K, V> entity : collections) {
            for (Entity<K, V> next = entity; next != null; next = next.next) {
                sb.append(next.key)
                        .append("=")
                        .append(next.value)
                        .append(",");
            }
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }*/

    public int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % collections.length);
    }

    private int hash(K key, int capacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    public int getArraySize() {
        return arraySize;
    }

    public Collections<K, V>[] getDataStructure() {
        return collections;
    }

    public int getCurrentCapacity() { return collections.length;}

}