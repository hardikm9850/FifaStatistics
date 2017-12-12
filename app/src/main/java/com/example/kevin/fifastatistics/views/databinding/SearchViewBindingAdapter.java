package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.databinding.adapters.SearchViewBindingAdapter.OnSuggestionClick;
import android.databinding.adapters.SearchViewBindingAdapter.OnSuggestionSelect;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Filterable;
import android.widget.ListAdapter;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

public class SearchViewBindingAdapter {

    private static final int SEARCH_SRC_VIEW = android.support.v7.appcompat.R.id.search_src_text;

    @BindingAdapter("textAdapter")
    public static <T extends ListAdapter & Filterable> void setAdapter(SearchView v, T adapter) {
        if (v != null) {
            SearchView.SearchAutoComplete searchSrcTextView = findAutoCompleteView(v);
            searchSrcTextView.setAdapter(adapter);
        }
    }

    private static SearchView.SearchAutoComplete findAutoCompleteView(SearchView v) {
        return (SearchView.SearchAutoComplete) v.findViewById(SEARCH_SRC_VIEW);
    }

    @BindingAdapter(value = {"android:onSuggestionSelect", "android:onSuggestionClick"},
            requireAll = false)
    public static void setOnSuggestListener(SearchView view, final OnSuggestionSelect submit, final OnSuggestionClick change) {
            if (submit == null && change == null) {
                view.setOnSuggestionListener(null);
            } else {
                view.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    @Override
                    public boolean onSuggestionSelect(int position) {
                        return submit != null && submit.onSuggestionSelect(position);
                    }

                    @Override
                    public boolean onSuggestionClick(int position) {
                        return change != null && change.onSuggestionClick(position);
                    }
                });
            }

    }

    @BindingAdapter("android:dropDownAnchor")
    public static void setDropDownAnchor(SearchView view, int anchor) {
        if (view != null) {
            SearchView.SearchAutoComplete searchSrcTextView = findAutoCompleteView(view);
            searchSrcTextView.setDropDownAnchor(anchor);
        }
    }

    @BindingAdapter("query")
    public static void setQuery(SearchView view, String query) {
        if (view != null) {
            view.setQuery(query, true);
        }
    }

    @BindingAdapter("textSize")
    public static void setTextSize(SearchView view, float textSize) {
        if (view != null) {
            SearchView.SearchAutoComplete searchSrcTextView = findAutoCompleteView(view);
            float sp = ResourceUtils.pixelsToSp(textSize);
            searchSrcTextView.setTextSize(sp);
        }
    }

    @BindingAdapter("android:onClearListener")
    public static void setOnClearListener(SearchView view, View.OnClickListener listener) {
        if (view != null) {
            View closeButton = view.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(listener);
        }
    }
}
