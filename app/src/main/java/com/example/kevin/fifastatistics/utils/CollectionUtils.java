package com.example.kevin.fifastatistics.utils;

import java.util.Collection;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static int getSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }
}
