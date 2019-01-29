package com.xenoire.moviefinder.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.db.Movie;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.xenoire.moviefinder.db.DatabaseContract.CONTENT_URI;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor movies;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private int favWidgetid;

    public FavoriteRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;
        favWidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("RemoteViewsFactory", "   CONSTRUCTOR");
    }

    @Override
    public void onCreate() {
        final long identityToken = Binder.clearCallingIdentity();
        movies = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (movies != null) {
            movies.moveToFirst();
            movieArrayList.clear();
            for (int i = 0; i < movies.getCount(); i++) {
                movieArrayList.add(getItem(i));
            }
        }
        Binder.restoreCallingIdentity(identityToken);
        Log.d("RemoteViewsFactory", "   ON CREATE");
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        movies = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (movies != null) {
            movies.moveToFirst();
            movieArrayList.clear();
            for (int i = 0; i < movies.getCount(); i++) {
                movieArrayList.add(getItem(i));
            }
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("RemoteViewsFactory", "   GetCount movies (cursor): " + movies.getCount());
        Log.d("RemoteViewsFactory", "   GetCount movies (ArrayList): " + movieArrayList.size());
        return movieArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_favorite_item);
        try {
            Bitmap banner = Glide.with(context)
                    .asBitmap()
                    .load(movieArrayList.get(i).getPosterUrl())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.iv_poster_widget, banner);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e("WidgetFav", "GLIDE ERROR");
        }
        rv.setTextViewText(R.id.tv_movieTitle_widget, movieArrayList.get(i).getTitle());
        Log.d("RemoteViewsFactory", "   getViewAt : " + movieArrayList.get(i).getTitle());

        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.MOVIE_ID, movieArrayList.get(i).getId());
        Log.d("RemoteViewsFactory", "ID" + movieArrayList.get(i).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(FavoriteMovieWidget.MOVIE_ID, extras);
        rv.setOnClickFillInIntent(R.id.iv_poster_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Movie getItem(int position) {
        if (!movies.moveToPosition(position)) {
            throw new IllegalStateException("Position is invalid");
        }
        return new Movie(movies);
    }
}
