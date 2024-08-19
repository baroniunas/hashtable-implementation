package com.baroniunas.collections;

public interface Collections<K,V> {

    public void put(K key, V value);
    public V getValue(K key);
    public V remove(K key);
}
