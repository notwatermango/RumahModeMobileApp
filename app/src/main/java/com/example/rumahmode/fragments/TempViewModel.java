package com.example.rumahmode.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TempViewModel extends ViewModel {

    // User id live data
    private MutableLiveData<Long> tempVal;

    // Constructor
    public TempViewModel() {
        tempVal = new MutableLiveData<>();
    }

    // Get user id live data
    public LiveData<Long> getTempVal() {
        return tempVal;
    }

    // Set user id live data
    public void setTempVal(long newTemp) {
        this.tempVal.setValue(newTemp);
    }
}
