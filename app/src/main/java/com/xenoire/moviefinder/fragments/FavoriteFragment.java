package com.xenoire.moviefinder.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.adapters.MyFavoriteAdapter;
import com.xenoire.moviefinder.db.FavoriteMovieHelper;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.loaders.FavoriteLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private OnFavoriteListFragmentItemClicked listener;

    private Cursor favoriteList;
    private int columnCount = 1;

    private RecyclerView rvFavorite;
    private ProgressBar loading;
    private TextView labelSad, labelNull;

    public static final String TAG = "Favorite Fragment";

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavorite = view.findViewById(R.id.rv_favorite);
        loading = view.findViewById(R.id.progressBarFavorite);
        labelNull = view.findViewById(R.id.label_null);
        labelSad = view.findViewById(R.id.label_sad);
        FavoriteMovieHelper favoriteMovieHelper = new FavoriteMovieHelper(getContext());
        favoriteMovieHelper.open();
        getLoaderManager().initLoader(3, null, FavoriteFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(3, null, FavoriteFragment.this);
        Log.d(TAG, "    ONRESUME()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        // Set the adapter
        if (view.findViewById(R.id.rv_favorite) instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.rv_favorite);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyFavoriteAdapter(favoriteList, listener, context));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteListFragmentItemClicked) {
            listener = (OnFavoriteListFragmentItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFavoriteListFragmentItemClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 3) {
            loading.setVisibility(View.VISIBLE);
            rvFavorite.setVisibility(View.INVISIBLE);
            Log.d(FavoriteFragment.TAG, "OnCreateLoader Start");
            return new FavoriteLoader(getContext());
        }
        Log.d(FavoriteFragment.TAG, "OnCreateLoader Null");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        MyFavoriteAdapter adapter = (MyFavoriteAdapter) rvFavorite.getAdapter();
        adapter.setMovies(data);
        adapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
        rvFavorite.setVisibility(View.VISIBLE);
        Log.d(FavoriteFragment.TAG, "OnLoadFinished");
        if (data.getCount() == 0) {
            labelSad.setVisibility(View.VISIBLE);
            labelNull.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onCreateLoader(3, null);
    }

    public interface OnFavoriteListFragmentItemClicked {
        // TODO: Update argument type and name
        void onFavoriteListItemClicked(Movie item);
    }

}
