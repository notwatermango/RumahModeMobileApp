package com.example.rumahmode.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    // User id live data
    private MutableLiveData<Long> user_id;

    // Constructor
    public UserViewModel() {
        user_id = new MutableLiveData<>();
    }

    // Get user id live data
    public LiveData<Long> getUser_id() {
        return user_id;
    }

    // Set user id live data
    public void setUser_id(long user_id) {
        this.user_id.setValue(user_id);
    }
}
