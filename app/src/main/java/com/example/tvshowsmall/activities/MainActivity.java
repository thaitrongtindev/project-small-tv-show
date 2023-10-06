package com.example.tvshowsmall.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tvshowsmall.R;
import com.example.tvshowsmall.dapters.TVShowsAdapter;
import com.example.tvshowsmall.databinding.ActivityMainBinding;
import com.example.tvshowsmall.models.TVShow;
import com.example.tvshowsmall.responses.TVShowResponse;
import com.example.tvshowsmall.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;

    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //inital activityMainBinding
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    }

    private void doInitalization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);

        //adapter
        tvShowsAdapter = new TVShowsAdapter(tvShows);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);



        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowsResponse
                -> Toast.makeText(getApplicationContext(), "Total Pages" + mostPopularTVShowsResponse.getTotalPages(), Toast.LENGTH_SHORT).show());

        activityMainBinding.setIsLoading(true);
        viewModel.getMostPopularTVShows(0).observe(this, new Observer<TVShowResponse>() {
            @Override
            public void onChanged(TVShowResponse tvShowResponse) {
                activityMainBinding.setIsLoading(false);
                if (tvShowResponse != null) {
                    tvShows.addAll(tvShowResponse.getTvShows());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}