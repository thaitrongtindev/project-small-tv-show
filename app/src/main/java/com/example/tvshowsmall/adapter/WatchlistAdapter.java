package com.example.tvshowsmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowsmall.R;
import com.example.tvshowsmall.databinding.ItemContainerTvShowBinding;
import com.example.tvshowsmall.listeners.WatchlistListener;
import com.example.tvshowsmall.models.TVShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {

    private List<TVShow> tvShowList ;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TVShow> tvShowList, WatchlistListener wa) {
        this.tvShowList = tvShowList;
        this.watchlistListener = wa;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show, parent,false
        );
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShowList.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {
        private ItemContainerTvShowBinding itemContainerTvShowBinding;
        public TVShowViewHolder(@NonNull ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TVShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
            itemContainerTvShowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                //.getRoot() được gọi để lấy ra View gốc của layout, nơi mà bạn muốn áp dụng sự kiện click.
                @Override
                public void onClick(View view) {
                    // xu ly khi minh nhan click vao bo phim, de xem chi tiet bo phim do
                    watchlistListener.onTVShowClicked(tvShow);
                }
            });
            itemContainerTvShowBinding.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchlistListener.removeTVShowFromWatchlist(tvShow, getAdapterPosition());
                }
            });

        }

    }
}
