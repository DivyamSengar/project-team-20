package edu.ucsd.cse110.successorator.util;

import androidx.lifecycle.MutableLiveData;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

/**
 * Customer adapter class for working with MutableLiveData and Subject classes
 */
public class MutableLiveDataSubjectAdapter<T>
        extends LiveDataSubjectAdapter<T>
        implements MutableSubject<T> {

    private final MutableLiveData<T> mutableAdaptee;

    /**
     * Constructor for the MutableLiveDataSubjectAdapter
     */
    public MutableLiveDataSubjectAdapter(MutableLiveData<T> adaptee) {
        super(adaptee);
        this.mutableAdaptee = adaptee;
    }

    /**
     * Setter for the value of the adapter
     *
     * @param value - the value to be set
     */
    @Override
    public void setValue(T value) {
        mutableAdaptee.setValue(value);
    }

}