package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.uber.autodispose.lifecycle.CorrespondingEventsFunction;
import com.uber.autodispose.lifecycle.LifecycleEndedException;
import com.uber.autodispose.lifecycle.LifecycleScopeProvider;
import com.uber.autodispose.lifecycle.LifecycleScopes;

import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class PresenterLifecycleScopeProvider implements LifecycleScopeProvider<PresenterLifecycleScopeProvider.Event> {

    enum Event {
        ON_CREATE, ON_DESTROY
    }

    @NonNull
    private final BehaviorSubject<Event> behaviorSubject;

    PresenterLifecycleScopeProvider() {
        behaviorSubject = BehaviorSubject.create();
    }

    public void onCreate() {
        behaviorSubject.onNext(Event.ON_CREATE);
    }

    public void onDestroy() {
        behaviorSubject.onNext(Event.ON_DESTROY);
    }

    @Override
    public Observable<Event> lifecycle() {
        return behaviorSubject;
    }

    @Override
    public CorrespondingEventsFunction<Event> correspondingEvents() {
        return event -> {
            switch (event) {
                case ON_CREATE:
                    return Event.ON_DESTROY;
                case ON_DESTROY:
                default:
                    throw new LifecycleEndedException("Lifecycle has ended! Last event was " + event);
            }
        };
    }

    @Override
    public Event peekLifecycle() {
        return behaviorSubject.getValue();
    }

    @Override
    public CompletableSource requestScope() throws Exception {
        return LifecycleScopes.resolveScopeFromLifecycle(this);
    }
}
