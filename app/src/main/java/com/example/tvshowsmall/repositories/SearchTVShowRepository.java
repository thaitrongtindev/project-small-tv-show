package com.example.tvshowsmall.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshowsmall.network.ApiClient;
import com.example.tvshowsmall.network.ApiService;
import com.example.tvshowsmall.responses.TVShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTVShowRepository {

    private ApiService apiService;

    public SearchTVShowRepository() {
        this.apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

        public LiveData<TVShowResponse> searchTVShow(String query, int page) {
        MutableLiveData<TVShowResponse> tvShowResponseMutableLiveData = new MutableLiveData<>();
        apiService.searchTVShow(query, page).enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowResponse> call, @NonNull Response<TVShowResponse> response) {
                tvShowResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowResponse> call, @NonNull Throwable t) {

            }
        });
        return tvShowResponseMutableLiveData;
    }
}
