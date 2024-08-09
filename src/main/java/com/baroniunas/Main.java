package com.baroniunas;

import com.baroniunas.hashtable.HashTable;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Integer> ht = new HashTable<>();

        ht.put("A", 2);
        ht.put("B", 3);
        System.out.println(ht);
        ht.put("C", 4);
        ht.put("D", 5);
        ht.put("E", 6);
        ht.put("F", 7);
        ht.put("F", 8);
        System.out.println(ht.getValue("E"));





    }
}