package com.example.kevin.fifastatistics.interfaces;

public interface AdapterInteraction<E> {

    void notifyNoMoreItemsToLoad();
    void notifyLoadingItems();
    void notifyItemsInserted(int startPosition, int numberOfItems);
}
