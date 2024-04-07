package com.example.tvshowsmall.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowsmall.database.TVShowDatabase;
import com.example.tvshowsmall.models.TVShow;
import com.example.tvshowsmall.repositories.TVShowDetailsRepository;
import com.example.tvshowsmall.responses.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends ViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;
    public TVShowDetailsViewModel(Application application) {
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
    public Completable addToWatchlist(TVShow tvShow) {
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }
    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId) {
        return tvShowDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }
    public Completable removeTVShowFromWatchlist(TVShow tvShow) {
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
