package com.baroniunas.collections.linkedlist;

public class Node<K,V> {
    final K key;
    V value;
    Node<K,V> next;

    public Node(K key, V value, Node<K,V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node<K, V> getNext() {
        return next;
    }

}
