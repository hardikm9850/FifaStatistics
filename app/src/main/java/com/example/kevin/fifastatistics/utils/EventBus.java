package com.example.kevin.fifastatistics.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class EventBus {

    private static final EventBus INSTANCE = new EventBus();

    public static EventBus getInstance() {
        return INSTANCE;
    }

    private final Subject<Object, Object> mEventBusSubject = createSubject();

    protected Subject<Object, Object> createSubject() {
        return new SerializedSubject<>(PublishSubject.create());
    }

    /**
     * Posts the provided {@param event} to the event bus.
     *
     * @param event     The event to be posted.
     */
    public <E> void post(E event) {
        if (event != null) {
            mEventBusSubject.onNext(event);
        }
    }

    public <E> Observable<E> observeEvents(Class<E> eventClass) {
        return mEventBusSubject.ofType(eventClass);
    }
}
