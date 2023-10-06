package com.example.tvshowsmall.network;

import com.example.tvshowsmall.responses.TVShowResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ApiClient {
    // https://www.episodate.com/api
    // https://www.episodate.com/api/most-popular?page=1
    public static final String URL = "https://www.episodate.com/api/";
    private static Retrofit retrofit;
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }


}
