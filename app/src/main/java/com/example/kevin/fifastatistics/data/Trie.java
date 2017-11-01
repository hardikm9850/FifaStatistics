package com.example.kevin.fifastatistics.data;

import com.example.kevin.fifastatistics.interfaces.Searchable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Autocomplete trie that returns searchable objects, rather than just the strings
 */
public class Trie<T extends Searchable> {

    private final Map<Character, Trie<T>> children;
    private String value;
    private T item = null;

    public Trie() {
        this(null);
    }

    private Trie(String value) {
        this.value = value;
        children = new HashMap<>();
    }

    /**
     * Insert all items in the collection to the trie.
     * @param items the items
     */
    public void insertAll(Collection<T> items) {
        if (items != null) {
            for (T item : items) {
                insert(item);
            }
        }
    }

    /**
     * Add an item to the trie. Null values are ignored.
     * @param item the item to add
     */
    public void insert(T item) {
        if (item == null || item.getSearchString() == null) {
            return;
        }
        Trie<T> node = this;
        String word = item.getSearchString();
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.add(c);
            }
            node = node.children.get(c);
        }
        node.item = item;
    }

    private void add(char c) {
        String val;
        if (this.value == null) {
            val = Character.toString(c);
        } else {
            val = this.value + c;
        }
        children.put(c, new Trie<>(val));
    }

    /**
     * Search for a specific item.
     * @param word the search string of the item to search for
     * @return the item, or null if not found
     */
    public T find(String word) {
        Trie<T> node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return null;
            }
            node = node.children.get(c);
        }
        return node.item;
    }

    /**
     * Return a collection of items that match the prefix.
     * @param prefix the search prefix
     * @return the items matching the prefix
     */
    public Collection<T> autoComplete(String prefix) {
        Trie<T> node = this;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        return node.allPrefixes();
    }

    /**
     * @return a collection of every item in the trie.
     */
    public Collection<T> allPrefixes() {
        List<T> results = new ArrayList<>();
        if (this.item != null) {
            results.add(this.item);
        }
        for (Entry<Character, Trie<T>> entry : children.entrySet()) {
            Trie<T> child = entry.getValue();
            Collection<T> childPrefixes = child.allPrefixes();
            results.addAll(childPrefixes);
        }
        return results;
    }
}