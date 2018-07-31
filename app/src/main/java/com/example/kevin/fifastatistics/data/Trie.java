package com.example.kevin.fifastatistics.data;

import com.example.kevin.fifastatistics.interfaces.Searchable;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Autocomplete trie that returns searchable objects, rather than just the strings
 * Case-insensitive, accent-insensitive
 */
public class Trie<T extends Searchable> implements Serializable {

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
        if (!doInsert(item, true)) {
            doInsert(item, false);
        }
    }

    private boolean doInsert(T item, boolean primaryString) {
        Trie<T> node = this;
        String accentedWord = primaryString ? item.getSearchString() : item.getSecondarySearchString();
        String word = stripAccents(accentedWord);
        boolean unique = false;
        for (char c : word.toCharArray()) {
            c = Character.toLowerCase(c);
            if (!node.children.containsKey(c)) {
                node.add(c);
                unique = true;
            }
            node = node.children.get(c);
        }
        if (unique) {
            node.item = item;
        }
        return unique;
    }

    private String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
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
            c = Character.toLowerCase(c);
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
    public List<T> autoComplete(String prefix) {
        Trie<T> node = this;
        for (char c : prefix.toCharArray()) {
            c = Character.toLowerCase(c);
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
    public List<T> allPrefixes() {
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