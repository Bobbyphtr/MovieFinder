package com.xenoire.moviefinder.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xenoire.moviefinder.db.FavoriteMovieHelper;

import static com.xenoire.moviefinder.db.DatabaseContract.AUTHORITY;
import static com.xenoire.moviefinder.db.DatabaseContract.CONTENT_URI;
import static com.xenoire.moviefinder.db.DatabaseContract.TABLE_NAME;

public class MovieFavoriteProvider extends ContentProvider {

    private final String TAG = MovieFavoriteProvider.class.getSimpleName();

    private static final int MOVIE_FAV = 1;
    private static final int MOVIE_FAV_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.xenoire.moviefinder/favoriteMovies
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE_FAV);
        //content://com.xenoire.moviefinder/favoriteMovies/id
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_FAV_ID);
    }

    private FavoriteMovieHelper favoriteMovieHelper;

    @Override
    public boolean onCreate() {
        favoriteMovieHelper = new FavoriteMovieHelper(getContext());
        favoriteMovieHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        Log.d(TAG, "        Query() " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAV:
                Log.d(TAG, "        Query() Movie_Fav");
                cursor = favoriteMovieHelper.queryProvider();
                break;
            case MOVIE_FAV_ID:
                Log.d(TAG, "        Query() Movie_Fav_ID");
                cursor = favoriteMovieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;
        Log.d(TAG, "        Insert() " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAV_ID:
                added = favoriteMovieHelper.insertProvider(contentValues);
                Log.d(TAG, "        Insert() Movie_Fav_ID");
                break;
            default:
                added = 0;
                break;
        }
        if (added > 0) getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        Log.d(TAG, "        Delete() " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAV_ID:
                Log.d(TAG, "        Delete() Movie_Fav_ID");
                deleted = favoriteMovieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        if (deleted > 0) getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated;
        switch (uriMatcher.match(uri)) {
            case MOVIE_FAV_ID:
                updated = favoriteMovieHelper.updateProvider(contentValues, uri.getLastPathSegment());
                break;
            default:
                updated = 0;
                break;
        }
        return updated;
    }
}
