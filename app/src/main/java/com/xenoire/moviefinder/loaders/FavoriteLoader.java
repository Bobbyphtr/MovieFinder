package com.xenoire.moviefinder.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import static com.xenoire.moviefinder.db.DatabaseContract.CONTENT_URI;

public class FavoriteLoader extends CursorLoader {

    private Cursor movies;
    private boolean hasResult = false;

    public FavoriteLoader(Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else if (hasResult) {
            deliverResult(movies);
        }
    }

    @Override
    public void deliverResult(Cursor cursor) {
        this.movies = cursor;
        hasResult = true;
        super.deliverResult(cursor);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            movies = null;
            hasResult = false;
        }
    }

    @Override
    public Cursor loadInBackground() {
        return getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
    }

}
