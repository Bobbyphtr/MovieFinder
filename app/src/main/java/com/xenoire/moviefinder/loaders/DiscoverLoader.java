package com.xenoire.moviefinder.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.xenoire.moviefinder.BuildConfig;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.DiscoverFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DiscoverLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private ArrayList<Movie> movies;
    private boolean hasResult = false;

    public static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private String searchQuery;

    public DiscoverLoader(@NonNull Context context, @Nullable String searchQuery) {
        super(context);
        onContentChanged();
        this.searchQuery = searchQuery;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(movies);
    }

    @Override
    public void deliverResult(@Nullable ArrayList<Movie> data) {
        this.movies = data;
        hasResult = true;
        super.deliverResult(data);
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

    @Nullable
    @Override
    public ArrayList<Movie> loadInBackground() {
        AsyncHttpClient client = new SyncHttpClient();
        final ArrayList<Movie> movies = new ArrayList<>();
        if (!TextUtils.isEmpty(searchQuery)) {
            String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&page=1&include_adult=true&query=" + searchQuery;
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject resultObj = new JSONObject(result);
                        JSONArray list = resultObj.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            Movie myMovie = new Movie(list.getJSONObject(i));
                            movies.add(myMovie);
                        }
                        Log.w(DiscoverFragment.TAG, "Success on fetch");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.w(DiscoverFragment.TAG, "Failure on fetch");
                }
            });
        } else {
            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject resultObj = new JSONObject(result);
                        JSONArray list = resultObj.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            Movie myMovie = new Movie(list.getJSONObject(i));
                            movies.add(myMovie);
                        }
                        Log.w(DiscoverFragment.TAG, "Success on fetch");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.w(DiscoverFragment.TAG, "Failure on fetch");
                }
            });
        }
        return movies;
    }
}
