package com.xenoire.moviefinder;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.squareup.picasso.Picasso;
import com.xenoire.moviefinder.db.FavoriteMovieHelper;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.loaders.DiscoverLoader;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.ORIGINAL_ID;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.POSTER_URL;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.TITLE;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_PARCEL = "movie";
    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView tvTitle, tvQuote, tvRating, tvDuration, tvLanguage, tvOverview, tvReleaseDate;
    private ChipCloud chipGenre;
    private ImageView ivPosterDetail;
    private ScrollView content;
    private ProgressBar loading;
    private Movie movieNow;

    private Uri uri;

    private Menu menu;

    private FavoriteMovieHelper helper;

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tv_titleDetail);
        tvQuote = findViewById(R.id.tv_quotesDetail);
        tvRating = findViewById(R.id.tv_ratingDetail);
        tvDuration = findViewById(R.id.tv_durationDetail);
        tvLanguage = findViewById(R.id.tv_languageDetail);
        tvOverview = findViewById(R.id.tv_overviewDetail);
        tvReleaseDate = findViewById(R.id.tv_releaseDate);
        chipGenre = findViewById(R.id.chip_genre);
        ivPosterDetail = findViewById(R.id.iv_poster_detail);
        content = findViewById(R.id.content);
        loading = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.detailCustomToolbar);
        setSupportActionBar(toolbar);

        helper = new FavoriteMovieHelper(this);

        uri = getIntent().getData();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Movie movie = getIntent().getParcelableExtra(MOVIE_PARCEL);
        setTitle(movie.getTitle());
        new ChipCloud.Configure()
                .chipCloud(chipGenre)
                .selectedColor(Color.parseColor("#B7673C"))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#3A5154"))
                .deselectedFontColor(Color.parseColor("#f2f2f2"))
                .selectTransitionMS(500)
                .gravity(ChipCloud.Gravity.LEFT)
                .mode(ChipCloud.Mode.MULTI)
                .labels(new String[]{})
                .build();
        LoadMovieAsync movieAsync = new LoadMovieAsync();
        movieAsync.execute(movie);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_favorite:
                if (!isLoading) {
                    Log.d(TAG, "movieNow : " + movieNow.getTitle());
                    if (movieNow.isFavorite()) {
                        helper.open();
                        item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        movieNow.setFavorite(false);
                        getContentResolver().delete(uri, ORIGINAL_ID + " = ", new String[]{String.valueOf(movieNow.getId())});
                        helper.close();
                        Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.removed_from_favorite), Snackbar.LENGTH_SHORT).show();
                    } else {
                        helper.open();
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                        movieNow.setFavorite(true);
                        ContentValues values = new ContentValues();
                        values.put(TITLE, movieNow.getTitle());
                        values.put(OVERVIEW, movieNow.getDescription());
                        values.put(POSTER_URL, movieNow.getPosterUrl());
                        values.put(ORIGINAL_ID, movieNow.getId());
                        getContentResolver().insert(uri, values);
                        helper.close();
                        Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.added_to_favorite), Snackbar.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class LoadMovieAsync extends AsyncTask<Movie, Integer, Movie> {

        static final String ASYNC_LOG = "loadMovieAsync";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            tvTitle.setText(movie.getTitle());
            tvQuote.setText(movie.getTagline());
            tvDuration.setText(movie.getDuration());
            tvRating.setText(movie.getRating());
            tvLanguage.setText(movie.getLang());
            tvReleaseDate.setText(movie.getReleaseDate());
            tvOverview.setText(movie.getDescription());
            Picasso.get().load(movie.getPosterUrl()).into(ivPosterDetail);
            for (String genre : movie.getGenres()) {
                chipGenre.addChip(genre);
            }
            loading.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            movieNow = movie;
            Log.d(ASYNC_LOG, "movieNow : " + movieNow.getTitle());
            isLoading = false;
            helper.open();
            Cursor query = getContentResolver().query(uri, null, null,
                    null, null);
            if (Objects.requireNonNull(query).getCount() > 0) {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_black_24dp);
                movieNow.setFavorite(true);
            }
            helper.close();
        }

        @Override
        protected Movie doInBackground(final Movie... movies) {
            Log.i(ASYNC_LOG, "Do in Background");
            SyncHttpClient client = new SyncHttpClient();
            String url = "https://api.themoviedb.org/3/movie/" + movies[0].getId() + "?api_key=" + DiscoverLoader.API_KEY + "&language=en-US";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    setUseSynchronousMode(true);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject objResult = new JSONObject(result);
                        movies[0].getDetails(objResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
            return movies[0];
        }
    }
}
