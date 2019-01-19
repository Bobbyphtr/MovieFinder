package com.xenoire.moviefinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.adapters.MyDiscoverAdapter;
import com.xenoire.moviefinder.loaders.DiscoverLoader;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    SearchView searchView;
    GridView gridView;
    MyDiscoverAdapter myDiscoverAdapter;
    ArrayList<Movie> movies;
    ProgressBar progressBar;

    private OnDiscoverItemClicked listener;
    public static final String TAG = "Discover Fragment";
    public static final String QUERY = "query";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchView = view.findViewById(R.id.searchView);
        gridView = view.findViewById(R.id.gv);
        progressBar = view.findViewById(R.id.progressBarMain);
        progressBar.setVisibility(View.GONE);
        movies = new ArrayList<>();
        myDiscoverAdapter = new MyDiscoverAdapter(movies, getActivity().getApplicationContext(), listener);
        gridView.setAdapter(myDiscoverAdapter);
        final String searchQuery = searchView.getQuery().toString();
        Bundle bundle = new Bundle();
        bundle.putString(QUERY, searchQuery);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String searchQuery = searchView.getQuery().toString();
                Bundle bundle = new Bundle();
                bundle.putString(QUERY, searchQuery);
                Log.i(DiscoverFragment.TAG, "OnCLICKED " + searchQuery);
                getLoaderManager().restartLoader(1, bundle, DiscoverFragment.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {
        if (i == 0) {
            progressBar.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            return new DiscoverLoader(getContext(), null);
        } else if (i == 1) {
            progressBar.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.INVISIBLE);
            return new DiscoverLoader(getContext(), bundle.getString(QUERY));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        myDiscoverAdapter.setMovies(data);
        myDiscoverAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        Log.w(DiscoverFragment.TAG, "OnLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        myDiscoverAdapter.setMovies(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gridView.setAdapter(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DiscoverFragment.OnDiscoverItemClicked) {
            listener = (DiscoverFragment.OnDiscoverItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNowPlayingListFragmentItemClicked");
        }
    }

    public interface OnDiscoverItemClicked {
        // TODO: Update argument type and name
        void onDiscoverItemClicked(Movie item);
    }
}
