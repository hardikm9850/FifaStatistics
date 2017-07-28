package com.example.kevin.fifastatistics.utils;

import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection != null && collection.size() == 0;
    }

    public static int getSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    /**
     * Removes an item from the list
     * @return the index of the removed item, or -1 if doesn't exist
     */
    public static int remove(List<?> list, Object item) {
        for (int i = 0; i < getSize(list); i++) {
            Object o = list.get(i);
            if (o != null && o.equals(item)) {
                list.remove(o);
                return i;
            }
        }
        return -1;
    }
}
