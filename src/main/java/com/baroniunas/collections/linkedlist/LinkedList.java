package com.baroniunas.collections.linkedlist;

import com.baroniunas.collections.Collections;


public class LinkedList<K extends Comparable<K>, V> implements Collections <K,V> {

    private Node<K,V> head;

    @Override
    public void put(K key, V value) {

        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value cannot be null");
        }

        if (head == null) {
            head = new Node<>(key, value, null);
            return;
        }

        Node<K,V> currentNode = head;
        Node<K,V> previousNode = null;

        while (currentNode != null) {
            if(currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        previousNode.next = new Node<>(key, value, null);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
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
