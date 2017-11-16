package com.frobro.bcindex.web.service;

import java.util.HashSet;
import java.util.Set;

public class Bset {
    public static Set<String> rightHalf(String[] arr) {
        return subSet(arr, arr.length/2, arr.length);
    }

    public static Set<String> leftHalf(String[] arr) {
        return subSet(arr, 0, arr.length/2);
    }

    private static Set<String> subSet(String[] arr, int start, int end) {
        Set<String> set = new HashSet<>();
        for (int i=start; i<end; i++) {
            set.add(new String(arr[i]));
        }
        return set;
    }
}
