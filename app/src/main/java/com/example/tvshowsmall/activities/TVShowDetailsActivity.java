package com.example.tvshowsmall.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;


import com.example.tvshowsmall.R;
import com.example.tvshowsmall.adapter.ImageSliderAdapter;
import com.example.tvshowsmall.databinding.ActivityTvshowDetailsBinding;
import com.example.tvshowsmall.responses.TVShowDetailsResponse;
import com.example.tvshowsmall.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);

        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {

        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());

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
                    activityTvshowDetailsBinding.setTvShowImageURL(tvShowDetailsResponse.getTvShowDetails().getImagePath());
//                    activityTvshowDetailsBinding.setDescription(String.valueOf(
//                            HtmlCompat.fromHtml(
//                                    tvShowDetailsResponse.getTvShowDetails().getDescription()
//                                    , HtmlCompat.FROM_HTML_MODE_LEGACY
//                            )
//                    ));
                    activityTvshowDetailsBinding.setDescription(tvShowDetailsResponse.getTvShowDetails().getDescription());

                    activityTvshowDetailsBinding.textReadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (activityTvshowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTvshowDetailsBinding. textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvshowDetailsBinding.textReadMore.setText("Read Less");
                            } else {
                                activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailsBinding.textReadMore.setText("Read More");

                            }
                        }
                    });
                    activityTvshowDetailsBinding.setRating(String.format(
                            Locale.getDefault(),
                            "%.2f",
                            Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                    ));
                    if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                        activityTvshowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                    } else {
                        activityTvshowDetailsBinding.setGenre("N/A");
                    }
                    activityTvshowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + "Min");
                    activityTvshowDetailsBinding.btnWebsite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        }
                    });
                    loadBasicTVShowDetails();
                }
            }
        });
    }

    private void loadImageSlider(String[] sliderImages) {

        //view pager 2
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        // circle indicator
        activityTvshowDetailsBinding.circleIndicator.setViewPager(activityTvshowDetailsBinding.sliderViewPager);

        //setting viewpager2
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(3);
        activityTvshowDetailsBinding.sliderViewPager.setClipToPadding(false);
        activityTvshowDetailsBinding.sliderViewPager.setClipChildren(false);

        // effect tranformer page
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        activityTvshowDetailsBinding.sliderViewPager.setPageTransformer(compositePageTransformer);

//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                int currentPosition = activityTvshowDetailsBinding.sliderViewPager.getCurrentItem();
//                if (currentPosition == sliderImages.length - 1) {
//                    activityTvshowDetailsBinding.sliderViewPager.setCurrentItem(0);
//                } else {
//                    activityTvshowDetailsBinding.sliderViewPager.setCurrentItem(currentPosition + 1);
//                }
//            }
//        };

        // listener event viewpager2 transfer page
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2000);
            }
        });

    }

//    private void setupSliderIndicator(int count)  {
//        ImageView[] indicator = new ImageView[count];
//        LinearLayout.LayoutParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//    }

    private void loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityTvshowDetailsBinding.setNetworkCountry(getIntent().getStringExtra("network") + " ( "
                + getIntent().getStringExtra("country") + ")");
        activityTvshowDetailsBinding.setStatus(getIntent().getStringExtra("status"));
        activityTvshowDetailsBinding.setStartDate(getIntent().getStringExtra("startDate"));
    }
}