package edu.ucsd.cse110.successorator.util;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Custom adapter to work with both LiveData and Subject
 */
public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adaptee;

    /**
     * Constructor for LiveDataSubjectAdapter
     *
     * @param adaptee - the passed in adapter
     */
    public LiveDataSubjectAdapter(LiveData<T> adaptee) {
        this.adaptee = adaptee;
    }

    /**
     * Gets the value of the adapter Subject
     */
    @Nullable
    @Override
    public T getValue() {
        return adaptee.getValue();
    }

    /**
     * Adds an observer to the adapter subject
     *
     * @param observer - the observer to be added
     */
    @Override
    public void observe(Observer<T> observer) {
        adaptee.observeForever(observer::onChanged);
    }

    /**
     * Removes an observer from the adapter Subject
     *
     * @param observer - the observer to be removed
     */
    @Override
    public void removeObserver(Observer<T> observer){
        adaptee.removeObserver(observer::onChanged);
    }
}