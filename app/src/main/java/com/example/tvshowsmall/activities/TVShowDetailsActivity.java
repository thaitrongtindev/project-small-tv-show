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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


import com.example.tvshowsmall.R;
import com.example.tvshowsmall.adapter.EpisodesAdapter;
import com.example.tvshowsmall.adapter.ImageSliderAdapter;
import com.example.tvshowsmall.databinding.ActivityTvshowDetailsBinding;
import com.example.tvshowsmall.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tvshowsmall.models.TVShow;
import com.example.tvshowsmall.responses.TVShowDetailsResponse;
import com.example.tvshowsmall.viewmodels.MyViewModelFactory;
import com.example.tvshowsmall.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private MyViewModelFactory factory;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private LayoutEpisodesBottomSheetBinding episodesBottomSheetBinding;
    private BottomSheetDialog episodeBottomSheetDiaglog;

    private TVShow tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_details);

        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        factory = new MyViewModelFactory(getApplication());
        tvShowDetailsViewModel = new ViewModelProvider(this, factory).get(TVShowDetailsViewModel.class);
        activityTvshowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        getTVShowsDetails();
    }

    private void getTVShowsDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
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
                                activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
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
                    // click
                    activityTvshowDetailsBinding.btnEpisodes.setOnClickListener(view -> {
                        Toast.makeText(TVShowDetailsActivity.this, "Click Episode", Toast.LENGTH_LONG).show();
                        episodeBottomSheetDiaglog = new BottomSheetDialog(TVShowDetailsActivity.this);
                        episodesBottomSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(TVShowDetailsActivity.this),
                                R.layout.layout_episodes_bottom_sheet, findViewById(R.id.episodesContainer), false);
                        episodeBottomSheetDiaglog.setContentView(episodesBottomSheetBinding.getRoot());
                        episodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                        );

                        episodesBottomSheetBinding.textTittle.setText(
                                String.format("Episodes | %s", tvShow.getName())
                        );
                        episodeBottomSheetDiaglog.show();

                        episodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> episodeBottomSheetDiaglog.dismiss());

                        // click add moive
                        // Gắn một OnClickListener vào biểu tượng imageWatchlist
                        activityTvshowDetailsBinding.imageWatchlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Tạo một CompositeDisposable để quản lý các tài nguyên
                                CompositeDisposable compositeDisposable = new CompositeDisposable();
                                Toast.makeText(TVShowDetailsActivity.this, "Click", Toast.LENGTH_LONG).show();
                                // Thực hiện thêm TV show vào danh sách theo dõi thông qua ViewModel
                                Disposable disposable = tvShowDetailsViewModel.addToWatchlist(tvShow)
                                        .subscribeOn(Schedulers.io()) // Thực hiện trên luồng I/O
                                        .observeOn(AndroidSchedulers.mainThread()) // Thực hiện trên luồng chính (UI)
                                        .subscribe(() -> {
                                            // Khi thêm vào danh sách thành công, cập nhật giao diện người dùng
                                            activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_check);
                                            // Hiển thị thông báo "Added to watchlist"
                                            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_LONG).show();
                                        }, throwable -> {
                                            // Xử lý khi có lỗi xảy ra (nếu cần)
                                            Log.e("TAG", "Error adding to watchlist: " + throwable.getMessage());
                                        });

                                // Thêm Disposable vào CompositeDisposable để quản lý
                                compositeDisposable.add(disposable);
                            }
                        });
                    });
                    activityTvshowDetailsBinding.imageWatchlist.setVisibility(View.VISIBLE);
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
        activityTvshowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvshowDetailsBinding.setNetworkCountry(tvShow.getNetwork() + " ( "
                + tvShow.getCountry() + ")");
        activityTvshowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvshowDetailsBinding.setStartDate(tvShow.getStartDate());
    }
}