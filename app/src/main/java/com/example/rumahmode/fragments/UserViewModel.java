package com.example.rumahmode.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private MutableLiveData<Long> user_id;

    public UserViewModel() {
        user_id = new MutableLiveData<>();
    }

    public LiveData<Long> getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id.setValue(user_id);
    }
}
