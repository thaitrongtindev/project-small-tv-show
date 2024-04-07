package com.example.tvshowsmall.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tvshowsmall.R;
import com.example.tvshowsmall.adapter.TVShowsAdapter;
import com.example.tvshowsmall.listeners.TVShowsListener;
import com.example.tvshowsmall.databinding.ActivityMainBinding;
import com.example.tvshowsmall.models.TVShow;
import com.example.tvshowsmall.responses.TVShowResponse;
import com.example.tvshowsmall.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TVShowsListener {

    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;

    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //inital activityMainBinding
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitalization();
    }

    private void doInitalization() {
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);

        //adapter
        tvShowsAdapter = new TVShowsAdapter(tvShows,this);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);

        //scroll view
        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowRecyclerView.canScrollVertically(1)) {
                    // kiểm tra xem danh sách có thể cuộn thêm xuống bên dưới hay không.
                    if (currentPage <= totalAvailablePage) {
                        currentPage += 1;
                        getMostPopularTVShows();
                    }
                }
            }
        });

        activityMainBinding.imageViewWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WatchlistActivity.class));
            }
        });

        activityMainBinding.imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows() {
//        viewModel.getMostPopularTVShows(0).observe(this, mostPopularTVShowsResponse
//                -> Toast.makeText(getApplicationContext(), "Total Pages" + mostPopularTVShowsResponse.getTotalPages(), Toast.LENGTH_SHORT).show());

       // activityMainBinding.setIsLoading(true);
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, new Observer<TVShowResponse>() {
            @Override
            public void onChanged(TVShowResponse tvShowResponse) {
             //   activityMainBinding.setIsLoading(false);
                toggleLoading();
                    totalAvailablePage = tvShowResponse.getTotalPages();// tong trong hien co
                if (tvShowResponse != null) {
                    int oldCount = tvShows.size(); // so phan tu cua TVshow tra ve
                    tvShows.addAll(tvShowResponse.getTvShows());
                    Log.e("TVShows", tvShowResponse.getTvShows().toString());
                    //tvShowsAdapter.notifyItemRangeChanged(oldCount, tvShows.size());
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()) {
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        } else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()) {
                activityMainBinding.setIsLoadingMore(false);
            } else  {
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}