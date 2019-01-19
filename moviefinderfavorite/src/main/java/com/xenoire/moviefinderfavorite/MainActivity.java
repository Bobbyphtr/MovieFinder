package com.xenoire.moviefinderfavorite;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.xenoire.moviefinderfavorite.adapters.FavoriteAdapter;
import com.xenoire.moviefinderfavorite.db.MovieItem;

import static com.xenoire.moviefinderfavorite.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements OnFavoriteItemClicked,
        LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView rvFavorite;
    FavoriteAdapter favoriteAdapter;
    private final int LOAD_FAVORITE = 1;
    Cursor movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvFavorite = findViewById(R.id.rv_favoriteList);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        favoriteAdapter = new FavoriteAdapter(movies, this, this);
        rvFavorite.setAdapter(favoriteAdapter);
        getSupportLoaderManager().initLoader(LOAD_FAVORITE, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        favoriteAdapter.setMovies(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(LOAD_FAVORITE, null, this);
    }

    @Override
    public void onFavoriteListItemClicked(MovieItem movie) {
        //no action
        Log.d("MAINFAVORITE", "Clicked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_FAVORITE);
    }
}


