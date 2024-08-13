package com.baroniunas;

import com.baroniunas.hashtable.HashTable;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Integer> ht = new HashTable<>();
/*
        ht.put("A", 2);
        ht.put("B", 3);
        ht.put("C", 4);
        ht.put("E", 4);
        ht.removePair("E");
        ht.removePair("B");
        ht.removePair("C");*/

        ht.put("A", 2);
        ht.put("B", 3);
        ht.put("C", 4);
        ht.put("E", 4);
        ht.put("O", 2);
        ht.put("Q", 10);
        ht.put("I", 7);
        ht.put("U", 8);
        ht.put("V", 6);
        ht.put("W", 5);
        ht.put("X", 4);
        ht.put("Y", 3);
        ht.put("Z", 1);
        System.out.println(ht.getValue("Z"));
        ht.put("Z", 1);
        ht.removePair("Z");










    }
}