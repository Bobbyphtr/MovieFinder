package com.xenoire.moviefinder.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.xenoire.moviefinder.BuildConfig;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.UpComingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UpComingLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private ArrayList<Movie> movies;
    private boolean hasResult = false;

    public UpComingLoader(@NonNull Context context) {
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
    public void deliverResult(ArrayList<Movie> data) {
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

    @Override
    public ArrayList<Movie> loadInBackground() {
        Log.w(UpComingFragment.TAG, "Load in Background");
        AsyncHttpClient client = new SyncHttpClient();
        final ArrayList<Movie> movies = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US";
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
                    Log.w(UpComingFragment.TAG, "Success on fetch");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.w(UpComingFragment.TAG, "Failure on fetch");
            }
        });
        return movies;
    }
}
