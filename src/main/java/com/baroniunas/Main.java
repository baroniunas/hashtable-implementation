package com.baroniunas;

import com.baroniunas.hashtable.HashTable;

public class Main {
    public static void main(String[] args) {
        HashTable<Integer, Integer> ht = new HashTable<>();
/*
        ht.put("A", 2);
        ht.put("B", 3);
        ht.put("C", 4);
        ht.put("E", 4);
        ht.removePair("E");
        ht.removePair("B");
        ht.removePair("C");*/

        ht.put(86, 2);
        ht.put(84, 3);
        ht.put(88, 3);

        System.out.println(ht.getArraySize());

    }
}