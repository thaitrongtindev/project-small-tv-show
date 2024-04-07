package com.example.tvshowsmall.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tvshowsmall.dao.TVShowDao;
import com.example.tvshowsmall.models.TVShow;
import com.example.tvshowsmall.models.TVShowDetails;


@Database(entities = TVShow.class, version = 1 , exportSchema = false)
public abstract class TVShowDatabase extends RoomDatabase {
    private static TVShowDatabase tvShowDatabase;
    public static synchronized TVShowDatabase getTvShowDatabase(Context context) {
        if (tvShowDatabase == null) {
            tvShowDatabase = Room.databaseBuilder(
                    context, TVShowDatabase.class,
                    "tv_shows_db"
            ).build();
        }
        return tvShowDatabase;
    }
    public abstract TVShowDao tvShowDao();
}
