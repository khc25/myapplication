package com.example.myapplication.ui.DailyTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailyTaskViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DailyTaskViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is DT fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}