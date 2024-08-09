package com.baroniunas.btree;

public class Node<K extends Comparable<K>, V> {
    public static final int MIN_DEGREE = 2;
    int numberOfKeys;
    Entry<K, V>[] keys;
    Node<K, V>[] children;
    boolean leaf;

    @SuppressWarnings("unchecked")
    public Node() {
        keys = new Entry[2 * MIN_DEGREE - 1];
        children = new Node[2 * MIN_DEGREE];
        leaf = true;
        numberOfKeys = 0;
    }

    public void insertNonFull(Entry<K, V> newEntry) {
        int index = numberOfKeys - 1;

        if (leaf) {
            while (index >= 0 && keys[index].key.compareTo(newEntry.key) > 0) {
                keys[index + 1] = keys[index];
                index--;
            }
            keys[index + 1] = newEntry;
            numberOfKeys++;
        } else {
            while (index >= 0 && keys[index].key.compareTo(newEntry.key) > 0) {
                index--;
            }
            index++;
            if (children[index].numberOfKeys == 2 * MIN_DEGREE - 1) {
                splitChild(index, children[index]);
                if (keys[index].key.compareTo(newEntry.key) < 0) {
                    index++;
                }
            }
            children[index].insertNonFull(newEntry);
        }
    }

    public void splitChild(int childIndex, Node<K, V> fullChild) {
        Node<K, V> newChild = new Node<>();
        newChild.leaf = fullChild.leaf;
        newChild.numberOfKeys = MIN_DEGREE - 1;

        for (int j = 0; j < MIN_DEGREE - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + MIN_DEGREE];
        }
        if (!fullChild.leaf) {
            for (int j = 0; j < MIN_DEGREE; j++) {
                newChild.children[j] = fullChild.children[j + MIN_DEGREE];
            }
        }
        fullChild.numberOfKeys = MIN_DEGREE - 1;

        for (int j = numberOfKeys; j >= childIndex + 1; j--) {
            children[j + 1] = children[j];
        }
        children[childIndex + 1] = newChild;

        for (int j = numberOfKeys - 1; j >= childIndex; j--) {
            keys[j + 1] = keys[j];
        }
        keys[childIndex] = fullChild.keys[MIN_DEGREE - 1];
        numberOfKeys++;
    }

    public Entry<K, V> search(K key) {
        int index = 0;
        while (index < numberOfKeys && key.compareTo(keys[index].key) > 0) {
            index++;
        }
        if (index < numberOfKeys && key.compareTo(keys[index].key) == 0) {
            return keys[index];
        }
        if (leaf) {
            return null;
        } else {
            return children[index].search(key);
        }
    }

    public void traverse() {
        int index;
        for (index = 0; index < numberOfKeys; index++) {
            if (!leaf) {
                children[index].traverse();
            }
            System.out.println(keys[index].key + ": " + keys[index].value);
        }
        if (!leaf) {
            children[index].traverse();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < numberOfKeys; i++) {
            sb.append(keys[i].key);
            if (i < numberOfKeys - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
