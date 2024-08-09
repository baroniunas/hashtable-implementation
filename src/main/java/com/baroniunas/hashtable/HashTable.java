package com.baroniunas.hashtable;

import com.baroniunas.btree.BTree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashTable<K extends Comparable<K>, V> {
    private static final int INITIAL_CAPACITY = 3;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int TREE_THRESHOLD = 6;
    private Entity<K, V>[] table;
    private int size;
    private BTree<K, V> bTree;

    private static final Logger logger = LogManager.getLogger(HashTable.class);

    @SuppressWarnings("unchecked")
    public HashTable() {
        table = new Entity[INITIAL_CAPACITY];
        logger.info("Initialized HashTable with capacity of {}.", INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        if (bTree != null) return;

        logger.info("Starting to resize tables capacity from {} to {}.", INITIAL_CAPACITY, newCapacity);
        Entity<K, V>[] oldTable = table;
        table = new Entity[newCapacity];
        size = 0;

        for (Entity<K, V> entity : oldTable) {
            while (entity != null) {
                put(entity.key, entity.value);
                entity = entity.next;
            }
        }
        logger.info("Resizing complete. New capacity: {}", newCapacity);
    }

    public void put(K key, V value) {

        if (bTree != null) {
            bTree.insert(key, value);
            return;
        }

        logger.debug("Adding new element with key -{}- to the table", key);
        if (key == null || value == null) {
            logger.error("Key or value is null. Cannot add to the table.");
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if ((float) size / table.length > LOAD_FACTOR) {
            logger.info("Table exceeded it's load factor. Resizing.. New capacity: {}", table.length);
            resize(table.length * 2);
        }

        int index = hash(key);
        Entity<K, V> newEntity = new Entity<>(key, value, null);

        if (table[index] == null) {
            table[index] = newEntity;
            logger.info("Inserted key -{}- with value {} at index: {}.", key, value, index);
        } else {
            Entity<K, V> currentEntity = table[index];
            Entity<K, V> previousEntity = null;
            while (currentEntity != null) {
                if (currentEntity.key.equals(key)) {
                    currentEntity.value = value;
                    logger.info("Updated key {} with value {} at index: {}.", key, value, index);
                    return;
                }
                previousEntity = currentEntity;
                currentEntity = currentEntity.next;
            }
            assert previousEntity != null;
            previousEntity.next = newEntity;
            logger.info("Inserted key {} with value {} at index: {} in chain.", key, value, index);
        }
        size++;
        logger.info("Element with key -{}- added successfully. Current size: {}", key, size);

        if (size == TREE_THRESHOLD) {
            transformToBTree();
        }
    }

    private void transformToBTree() {
        bTree = new BTree<>();
        for (Entity<K, V> entity : table) {
            while (entity != null) {
                bTree.insert(entity.key, entity.value);
                entity = entity.next;
            }
        }
        table = null;
    }

    public V getValue(K key) {

        if (key == null) {
            logger.error("Key is null. Cannot retrieve from the table.");
            throw new IllegalArgumentException("Key cannot be null");
        }

        if (bTree != null) {
            return bTree.search(key);
        }

        logger.debug("Attempting to get element with key -{}- from the table", key);
        int index = hash(key);
        Entity<K, V> currentEntity = table[index];

        while (currentEntity != null) {
            if (currentEntity.key.equals(key)) {
                logger.info("Element with key -{}- found and index {}.", key, index);
                return currentEntity.value;
            }
            currentEntity = currentEntity.next;
        }
        logger.warn("Element with key -{}- not found in the table", key);
        return null;
    }

    public V removePair(K key) {
        logger.debug("Attempting to remove element with key -{}- from the table", key);
        if (key == null) {
            logger.error("Key is null. Cannot remove from the table.");
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        Entity<K, V> currentEntity = table[index];
        Entity<K, V> previousEntity = null;

        while (currentEntity != null) {
            if (currentEntity.key.equals(key)) {
                if (previousEntity == null) {
                    table[index] = currentEntity.next;
                } else {
                    previousEntity.next = currentEntity.next;
                }
                size--;
                logger.info("Element with key -{}- removed successfully. Current size: {}", key, size);
                if (size <= table.length / 4 && table.length > INITIAL_CAPACITY) {
                    logger.info("Table size decreased, shrinking to smaller capacity..");
                    resize(table.length / 2);
                }
                return currentEntity.value;
            }
            previousEntity = currentEntity;
            currentEntity = currentEntity.next;
        }
        logger.warn("Element with key -{}- not found in the table", key);
        return null;
    }

    public void printTree() {
        bTree.traverse();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Entity<K, V> entity : table) {
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
    }

    public int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public int getSize() {
        return size;
    }

    public BTree<K, V> getBTree() {
        return bTree;
    }

    public Entity<K, V>[] getTable() {
        return table;
    }

    public int getCurrentCapacity() { return table.length;}

    private static class Entity<K, V> {
        final K key;
        V value;
        Entity<K, V> next;

        Entity(K key, V value, Entity<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}