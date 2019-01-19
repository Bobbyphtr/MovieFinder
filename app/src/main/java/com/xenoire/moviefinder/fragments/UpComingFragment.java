package com.xenoire.moviefinder.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.adapters.MyUpComingAdapter;
import com.xenoire.moviefinder.loaders.UpComingLoader;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private OnUpComingListFragmentItemClicked listener;

    private int columnCount = 2;

    private RecyclerView rvUpComing;
    private ProgressBar loading;

    public static final String TAG = "UpComing Fragment";

    public UpComingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvUpComing = view.findViewById(R.id.rv_upComing);
        loading = view.findViewById(R.id.progressBarUpComing);
        getLoaderManager().initLoader(3, null, UpComingFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_coming, container, false);
        ArrayList<Movie> upComingMovies = new ArrayList<>();
        // Set the adapter
        if (view.findViewById(R.id.rv_upComing) instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.rv_upComing);
            if (columnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            recyclerView.setAdapter(new MyUpComingAdapter(upComingMovies, listener, context));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpComingListFragmentItemClicked) {
            listener = (OnUpComingListFragmentItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNowPlayingListFragmentItemClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        if (id == 3) {
            loading.setVisibility(View.VISIBLE);
            rvUpComing.setVisibility(View.INVISIBLE);
            Log.d(UpComingFragment.TAG, "OnCreateLoader Start");
            return new UpComingLoader(getContext());
        }
        Log.d(UpComingFragment.TAG, "OnCreateLoader Null");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        MyUpComingAdapter adapter = (MyUpComingAdapter) rvUpComing.getAdapter();
        adapter.setMovies(data);
        adapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
        rvUpComing.setVisibility(View.VISIBLE);
        Log.d(UpComingFragment.TAG, "OnLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        MyUpComingAdapter adapter = (MyUpComingAdapter) rvUpComing.getAdapter();
        adapter.setMovies(null);
        Log.d(UpComingFragment.TAG, "OnLoaderReset");
    }

    public interface OnUpComingListFragmentItemClicked {
        // TODO: Update argument type and name
        void onUpComingListItemClicked(Movie item);
    }

}
