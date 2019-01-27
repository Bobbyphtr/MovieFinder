package com.xenoire.moviefinder.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xenoire.moviefinder.R;

import java.util.concurrent.ExecutionException;

import static com.xenoire.moviefinder.db.DatabaseContract.CONTENT_URI;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.POSTER_URL;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.TITLE;

public class FavoriteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor movies;
    private int favWidgetid;

    public FavoriteRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        favWidgetid = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("RemoteViewsFactory", "   CONSTRUCTOR");
    }

    @Override
    public void onCreate() {
        final long identityToken = Binder.clearCallingIdentity();
        movies = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
        Log.d("RemoteViewsFactory", "   ON CREATE");
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        movies = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("RemoteViewsFactory", "   GetCount : " + movies.getCount());
        return movies.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_favorite_item);
        movies.move(i);
        try {
            Bitmap banner = Glide.with(context)
                    .asBitmap()
                    .load(movies.getString(movies.getColumnIndexOrThrow(POSTER_URL)))
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.iv_poster_widget, banner);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.e("WidgetFav", "GLIDE ERROR");
        }
        rv.setTextViewText(R.id.tv_movieTitle_widget, movies.getString(movies.getColumnIndexOrThrow(TITLE)));
        Log.d("RemoteViewsFactory", "   getViewAt : " + movies.getString(movies.getColumnIndexOrThrow(TITLE)));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d("RemoteViewsFactory", "   GetViewTypeCount()");
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
}
