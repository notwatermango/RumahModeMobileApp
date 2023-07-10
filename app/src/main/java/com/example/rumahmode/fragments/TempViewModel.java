package com.example.rumahmode.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TempViewModel extends ViewModel {

    private MutableLiveData<Long> tempVal;

    public TempViewModel() {
        tempVal = new MutableLiveData<>();
    }

    public LiveData<Long> getTempVal() {
        return tempVal;
    }

    public void setTempVal(long newTemp) {
        this.tempVal.setValue(newTemp);
    }
}
