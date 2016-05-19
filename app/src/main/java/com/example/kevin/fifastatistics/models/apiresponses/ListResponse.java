package com.example.kevin.fifastatistics.models.apiresponses;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic model of a list response from the API (e.g., get users, get matches,
 * etc.). Expects data of the following format:
 * <pre>
 * {@code
 * "_embedded": {
 *   "<items>": [
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
public class ListResponse<T>
{
    @JsonProperty("_embedded")
    private Embedded<T> embedded;

    public ArrayList<T> getItems() {
        return embedded.getItems();
    }

    public static class Embedded<T> {

        private String name;
        private Map<String, ArrayList<T>> items = new HashMap<>();

        public ArrayList<T> getItems() {
            return items.get(name);
        }

        @JsonAnySetter
        public void setItems(String name, ArrayList<T> results) {
            this.name = name;
            items.put(name, results);
        }
    }
}
