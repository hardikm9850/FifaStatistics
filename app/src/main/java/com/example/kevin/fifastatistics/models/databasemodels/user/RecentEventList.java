package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper of a collection that displays the most recent events (Matches, Series, MatchStubs, etc.)
 */
public class RecentEventList<E> {

    private static final int RECENT_EVENT_SIZE = 5;

    private List<E> events;

    public RecentEventList() {
       events = new LinkedList<>();
    }

    @SuppressWarnings("unused") // used during serialization
    @JsonCreator
    private RecentEventList(List<E> events) {
        this.events = events;
    }

    public void add(E element) {
        if (events.size() >=  RECENT_EVENT_SIZE) {
            events.remove(0);
        }
        events.add(element);
    }

    public E get(int index) {
        return events.get(index);
    }

    @SuppressWarnings("unused") // used to flatten to list during serialization
    @JsonValue
    private List<E> getEvents() {
        return events;
    }
}
