package com.baroniunas.btree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Node<K extends Comparable<K>, V> {
    public static final int MIN_DEGREE = 2;
    int numberOfKeys;
    public Entry<K, V>[] entries;
    Node<K, V>[] children;
    boolean leaf;

    private static final Logger logger = LogManager.getLogger(Node.class);

    @SuppressWarnings("unchecked")
    public Node() {
        entries = new Entry[2 * MIN_DEGREE - 1];
        children = new Node[2 * MIN_DEGREE];
        leaf = true;
        numberOfKeys = 0;
    }

    public void insertNonFull(Entry<K, V> newEntry) {
        int index = numberOfKeys - 1;

        if (leaf) {
            while (index >= 0 && entries[index].key.compareTo(newEntry.key) > 0) {
                entries[index + 1] = entries[index];
                index--;
            }
            entries[index + 1] = newEntry;
            numberOfKeys++;
        } else {
            while (index >= 0 && entries[index].key.compareTo(newEntry.key) > 0) {
                index--;
            }
            index++;
            if (children[index].numberOfKeys == 2 * MIN_DEGREE - 1) {
                splitChild(index, children[index]);
                if (entries[index].key.compareTo(newEntry.key) < 0) {
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
            newChild.entries[j] = fullChild.entries[j + MIN_DEGREE];
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
            entries[j + 1] = entries[j];
        }
        entries[childIndex] = fullChild.entries[MIN_DEGREE - 1];
        numberOfKeys++;
    }

    public void delete(K key) {
        int index = findKeyIndex(key);
        logger.info("Deleting <{}> key from {} index", key, index);
        if (index < numberOfKeys && entries[index].key.compareTo(key) == 0) {
            if (leaf) {
                // Case 1a: The node is a leaf node
                removeFromLeaf(index);
            } else {
                // Case 1b: The node is an internal node
                removeFromNonLeaf(index);
            }
        } else {
            // Case 2: Key is not found in this node
            if (leaf) {
                logger.info("Key {} is not present in the tree", key);
                return;
            }

            // Determine if the key is in the subtree rooted with the last child
            boolean flag = (index == numberOfKeys);

            // Ensure the child where the key may exist has at least MIN_DEGREE entries
            if (children[index].numberOfKeys < MIN_DEGREE) {
                fill(index);
            }

            // If the last child has been merged, it must have been merged with the previous child, so recurse on the (idx-1)th child
            if (flag && index > numberOfKeys) {
                children[index - 1].delete(key);
            } else {
                children[index].delete(key);
            }
        }
    }

    private void removeFromLeaf(int index) {
        for (int i = index + 1; i < numberOfKeys; ++i) {
            entries[i - 1] = entries[i];
        }
        numberOfKeys--;
    }

    private void removeFromNonLeaf(int index) {
        K key = entries[index].key;

        // Case 2a: The child before key (C[idx]) has at least MIN_DEGREE entries
        if (children[index].numberOfKeys >= MIN_DEGREE) {
            Entry<K, V> predecessor = getPredecessor(index);
            entries[index] = predecessor;
            children[index].delete(predecessor.key);
        }

        // Case 2b: The child after key (C[idx+1]) has at least MIN_DEGREE entries
        else if (children[index + 1].numberOfKeys >= MIN_DEGREE) {
            Entry<K, V> successor = getSuccessor(index);
            entries[index] = successor;
            children[index + 1].delete(successor.key);
        }

        // Case 2c: Both C[idx] and C[idx+1] have less than MIN_DEGREE entries
        else {
            merge(index);
            children[index].delete(key);
        }
    }

    private Entry<K, V> getPredecessor(int index) {
        Node<K, V> current = children[index];
        while (!current.leaf) {
            current = current.children[current.numberOfKeys];
        }
        return current.entries[current.numberOfKeys - 1];
    }

    private Entry<K, V> getSuccessor(int index) {
        Node<K, V> current = children[index + 1];
        while (!current.leaf) {
            current = current.children[0];
        }
        return current.entries[0];
    }

    private void fill(int index) {
        if (index != 0 && children[index - 1].numberOfKeys >= MIN_DEGREE) {
            borrowFromPrevious(index);
        } else if (index != numberOfKeys && children[index + 1].numberOfKeys >= MIN_DEGREE) {
            borrowFromNext(index);
        } else {
            if (index != numberOfKeys) {
                merge(index);
            } else {
                merge(index - 1);
            }
        }
    }

    public void borrowFromPrevious(int index) {
        Node<K, V> child = children[index];
        Node<K, V> sibling = children[index - 1];

        for (int i = child.numberOfKeys -1; i >= 0; i--) {
            child.entries[i + 1] = child.entries[i];
        }

        if(!child.leaf) {
            for (int i = child.numberOfKeys - 1; i >= 0; i--) {
                child.children[i + 1] = child.children[i];
            }
        }

        child.entries[0] = entries[index - 1];

        if (!leaf) {
            child.children[0] = sibling.children[sibling.numberOfKeys];
        }
        entries[index - 1] = sibling.entries[sibling.numberOfKeys - 1];
        child.numberOfKeys += 1;
        sibling.numberOfKeys -= 1;
    }

    public void borrowFromNext(int index) {
        logger.debug("Borrowing from next sibling for node at index {}", index);
        Node<K, V> child = children[index];
        Node<K, V> sibling = children[index + 1];

        child.entries[child.numberOfKeys] = entries[index];

        if (!child.leaf) {
            child.children[child.numberOfKeys + 1] = sibling.children[0];
        }

        entries[index] = sibling.entries[0];

        for (int i = 1; i < sibling.numberOfKeys; i++) {
            sibling.entries[i - 1] = sibling.entries[i];
        }

        if (!sibling.leaf) {
            for (int i = 1; i <= sibling.numberOfKeys; i++) {
                sibling.children[i - 1] = sibling.children[i];
            }
        }

        child.numberOfKeys += 1;
        sibling.numberOfKeys -= 1;

    }

    public void merge(int index) {
        logger.debug("Merging node at index {}", index);
        Node<K, V> child = children[index];
        Node<K, V> sibling = children[index + 1];

        child.entries[MIN_DEGREE - 1] = entries[index];

        for (int i = 0; i < sibling.numberOfKeys; i++) {
            child.entries[i + MIN_DEGREE] = sibling.entries[i];
        }

        if (!child.leaf) {
            for (int i = 0; i <= sibling.numberOfKeys; i++) {
                child.children[i + MIN_DEGREE] = sibling.children[i];
            }
        }

        for (int i = index + 1; i < numberOfKeys; i++) {
            entries[i - 1] = entries[i];
        }

        for (int i = index + 2; i <= numberOfKeys; i++) {
            children[i - 1] = children[i];
        }

        child.numberOfKeys += sibling.numberOfKeys + 1;
        numberOfKeys--;
        logger.info("After merge, node: {}", this);
    }

    public int findKeyIndex(K key) {
        int index = 0;
        while (index < numberOfKeys && entries[index].key.compareTo(key) < 0) {
            index++;
        }
        return index;
    }

    public Entry<K, V> search(K key) {
        int index = 0;
        while (index < numberOfKeys && key.compareTo(entries[index].key) > 0) {
            index++;
        }
        if (index < numberOfKeys && key.compareTo(entries[index].key) == 0) {
            return entries[index];
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
            System.out.println(entries[index].key + ": " + entries[index].value);
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
            sb.append(entries[i].key);
            if (i < numberOfKeys - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }


}
