package com.baroniunas.collections;

public interface Collections<K,V> {

    void put(K key, V value);
    V getValue(K key);
    V remove(K key);
    int getSize();
    boolean containsKey(K key);
}
