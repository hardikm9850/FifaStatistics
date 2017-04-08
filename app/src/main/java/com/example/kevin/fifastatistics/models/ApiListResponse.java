package com.example.kevin.fifastatistics.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * Generic model of a list response from the API (e.g., get users, get matches,
 * etc.). Expects data of the following format:
 * <pre>
 * {@code
 * "_embedded": {
 *   "items": [
 *      {
 *          ... // list of items
 *      }
 *   }
 * }
 * </pre>
 * where <code>"items"</code> is actually <code>"users"</code>, <code>"matches"</code>,
 * etc.
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiListResponse<T> {

    @JsonProperty("_embedded")
    private Embedded<T> embedded;

    @JsonProperty("_links")
    private Links links;

    public List<T> getItems() {
        return embedded.getItems();
    }

    public Links getLinks() {
        return links;
    }

    public boolean hasNext() {
        return getNext() != null;
    }

    public String getNext() {
        return links != null ? (links.getNext() != null ? links.getNext().getHref() : null) : null;
    }

    public static class Embedded<T> {

        private String name;
        private Map<String, List<T>> items = new HashMap<>();

        List<T> getItems() {
            return items.get(name);
        }

        @JsonAnySetter
        public void setItems(String name, List<T> results) {
            this.name = name;
            items.put(name, results);
        }
    }

    @Getter
    public static class Links implements Serializable {
        private Link first;
        private Link self;
        private Link next;
        private Link prev;
        private Link last;
        private Link profile;
        private Link search;
        private Link teams;
        private Link league;
    }

    @Getter
    public static class Link implements Serializable {
        private String href;
    }
}
