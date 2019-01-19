package com.xenoire.moviefinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.DiscoverFragment;
import com.xenoire.moviefinder.fragments.FavoriteFragment;
import com.xenoire.moviefinder.fragments.NowPlayingFragment;
import com.xenoire.moviefinder.fragments.UpComingFragment;

import static com.xenoire.moviefinder.db.DatabaseContract.CONTENT_URI;


public class MainActivity extends AppCompatActivity implements NowPlayingFragment.OnNowPlayingListFragmentItemClicked,
        DiscoverFragment.OnDiscoverItemClicked, UpComingFragment.OnUpComingListFragmentItemClicked,
        FavoriteFragment.OnFavoriteListFragmentItemClicked {
    public static final String TAG = "MovieFinder";
    BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction;
            switch (menuItem.getItemId()) {
                case R.id.item_nav_discover:
                    toolbar_title.setText(getResources().getString(R.string.discover));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new DiscoverFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.item_nav_nowPlaying:
                    toolbar_title.setText(getResources().getString(R.string.now_playing));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new NowPlayingFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.item_nav_upComing:
                    toolbar_title.setText(getResources().getString(R.string.up_coming));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new UpComingFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.item_nav_favorite:
                    toolbar_title.setText(getResources().getString(R.string.favorite));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new FavoriteFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.custom_appbar);
        toolbar_title = toolbar.findViewById(R.id.appBar_title);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState != null && savedInstanceState.getInt(CURRENT_FRAGMENT_KEY, 0) != 0) {
            switch (savedInstanceState.getInt(CURRENT_FRAGMENT_KEY)) {
                case R.id.item_nav_nowPlaying:
                    toolbar_title.setText(getResources().getString(R.string.now_playing));
                    bottomNavigationView.setSelectedItemId(R.id.item_nav_nowPlaying);
                    fragmentTransaction.replace(R.id.fragment_container, new NowPlayingFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.item_nav_discover:
                    toolbar_title.setText(getResources().getString(R.string.discover));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new DiscoverFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.item_nav_favorite:
                    toolbar_title.setText(getResources().getString(R.string.favorite));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new FavoriteFragment());
                    fragmentTransaction.commit();
                    break;
                case R.id.item_nav_upComing:
                    toolbar_title.setText(getResources().getString(R.string.up_coming));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new UpComingFragment());
                    fragmentTransaction.commit();
                    break;
            }
        } else {
            toolbar_title.setText(getResources().getString(R.string.now_playing));
            bottomNavigationView.setSelectedItemId(R.id.item_nav_nowPlaying);
            fragmentTransaction.replace(R.id.fragment_container, new NowPlayingFragment());
            fragmentTransaction.commit();
        }
    }

    private final String CURRENT_FRAGMENT_KEY = "current_fragment";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_KEY, bottomNavigationView.getSelectedItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_changeLanguage:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNowPlayingListItemClicked(Movie item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_PARCEL, item);
        Uri uri = Uri.parse(CONTENT_URI + "/" + item.getId());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onDiscoverItemClicked(Movie item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_PARCEL, item);
        Uri uri = Uri.parse(CONTENT_URI + "/" + item.getId());
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onUpComingListItemClicked(Movie item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_PARCEL, item);
        Uri uri = Uri.parse(CONTENT_URI + "/" + item.getId());
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onFavoriteListItemClicked(Movie item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_PARCEL, item);
        Uri uri = Uri.parse(CONTENT_URI + "/" + item.getId());
        intent.setData(uri);
        startActivity(intent);
    }
}
