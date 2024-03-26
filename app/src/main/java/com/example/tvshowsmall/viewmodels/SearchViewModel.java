package com.example.tvshowsmall.viewmodels;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowsmall.repositories.SearchTVShowRepository;
import com.example.tvshowsmall.responses.TVShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        this.searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowResponse> searchTVshow(String query, int page) {
        return searchTVShowRepository.searchTVShow(query, page);
    }
}
