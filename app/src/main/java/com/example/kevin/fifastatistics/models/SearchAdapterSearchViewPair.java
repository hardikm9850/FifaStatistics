package com.example.kevin.fifastatistics.models;

import com.example.kevin.fifastatistics.views.adapters.SearchViewAdapter;
import com.lapism.searchview.view.SearchView;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Container of a SearchView and the Search Adapter to be associated with it.
 */
@Getter
@RequiredArgsConstructor
public class SearchAdapterSearchViewPair {

    private final SearchView searchView;
    private final SearchViewAdapter adapter;
}
