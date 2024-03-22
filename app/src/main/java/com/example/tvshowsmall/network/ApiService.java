package com.example.tvshowsmall.network;

import com.example.tvshowsmall.responses.TVShowDetailsResponse;
import com.example.tvshowsmall.responses.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //https://www.episodate.com/api/most-popular
    //https://www.episodate.com/api/most-popular?page=1
    @GET("most-popular")
    Call<TVShowResponse> getMostPopularTVShows(@Query("page") int page);

    //https://www.episodate.com/api/show-details?q=arrow
    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);
}
