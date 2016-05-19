package com.example.kevin.fifastatistics.models;

import com.example.kevin.fifastatistics.views.adapters.SearchViewAdapter;
import com.lapism.searchview.view.SearchView;

/**
 * Container of a SearchView and the Search Adapter to be associated with it.
 */
public class SearchAdapterSearchViewPair {

    private SearchView searchView;
    private SearchViewAdapter adapter;

    public SearchAdapterSearchViewPair(SearchView searchView, SearchViewAdapter adapter) {
        this.searchView = searchView;
        this.adapter = adapter;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public SearchViewAdapter getAdapter() {
        return adapter;
    }
}
