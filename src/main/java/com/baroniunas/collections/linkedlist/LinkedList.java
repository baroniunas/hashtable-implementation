package com.baroniunas.collections.linkedlist;

import com.baroniunas.collections.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LinkedList<K extends Comparable<K>, V> implements Collections <K,V> {

    private Node<K,V> head;
    private int size = 0;

    private static final Logger logger = LogManager.getLogger(LinkedList.class);

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if (head == null) {
            head = new Node<>(key, value, null);
            size++;
            return;
        }

        Node<K,V> currentNode = head;
        while (currentNode != null) {
            if(currentNode.key.equals(key)) {
                logger.debug("Updating existing key <{}>, value to <{}>.", key, value);
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                logger.debug("Adding new pair key <{}>, value <{}> to LinkedList", key, value);
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        logger.debug("Getting value from <{}> key.", key);
        Node<K,V> currentNode = head;
        while (currentNode != null) {
            if(currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        Node<K,V> currentNode = head;
        while (currentNode != null) {
            if(currentNode.key.equals(key)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public int getSize() {
      return size;
    }

    @Override
    public V remove(K key) {

        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        Node<K,V> currentNode = head;
        Node<K,V> previousNode = null;
        while (currentNode != null) {
            if(currentNode.key.equals(key)) {
                if(previousNode == null) {
                    head = currentNode.next;
                } else {
                    previousNode.next = currentNode.next;
                }
                size--;
                return currentNode.value;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        return null;
    }

    public Node<K,V> getHead() {
        return head;
    }
}
