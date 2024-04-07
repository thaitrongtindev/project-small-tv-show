package com.example.tvshowsmall.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private  final Application application;

    public MyViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TVShowDetailsViewModel.class)) {
            return (T) new TVShowDetailsViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
