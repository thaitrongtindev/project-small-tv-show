package com.example.tvshowsmall.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.tvshowsmall.R;
import com.example.tvshowsmall.adapter.ImageSliderAdapter;
import com.example.tvshowsmall.databinding.ActivityTvshowDetailsBinding;
import com.example.tvshowsmall.responses.TVShowDetailsResponse;
import com.example.tvshowsmall.viewmodels.TVShowDetailsViewModel;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);

        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {

        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        getTVShowsDetails();
    }

    private void getTVShowsDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, new Observer<TVShowDetailsResponse>() {
            @Override
            public void onChanged(TVShowDetailsResponse tvShowDetailsResponse) {
                activityTvshowDetailsBinding.setIsLoading(false);
                if (tvShowDetailsResponse.getTvShowDetails() != null) {
                    if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {

                        // lay url anh tu api roi hien thi slideImage
                        loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                    }
                }
            }
        });
    }

    private void loadImageSlider(String []sliderImages) {
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));

    }
}