package com.xenoire.moviefinder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.adapters.MyNowPlayingAdapter;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.loaders.NowPlayingLoader;

import java.util.ArrayList;

import static com.xenoire.moviefinder.fragments.DiscoverFragment.CHANGE_STATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNowPlayingListFragmentItemClicked}
 * interface.
 */
public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private ArrayList<Movie> nowPlayingMovies;

    private RecyclerView rvNowPlaying;
    private ProgressBar loading;

    public static final String TAG = "NowPlaying Fragment";

    private OnNowPlayingListFragmentItemClicked mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NowPlayingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvNowPlaying = view.findViewById(R.id.rv_nowPlaying);
        loading = view.findViewById(R.id.progressBarNowPlaying);
        getLoaderManager().initLoader(2, null, NowPlayingFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nowplaying_list, container, false);
        nowPlayingMovies = new ArrayList<>();
        // Set the adapter
        if (view.findViewById(R.id.rv_nowPlaying) instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.rv_nowPlaying);
            // TODO: Customize parameters
            int mColumnCount = 2;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyNowPlayingAdapter(nowPlayingMovies, mListener, getActivity().getApplicationContext()));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNowPlayingListFragmentItemClicked) {
            mListener = (OnNowPlayingListFragmentItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNowPlayingListFragmentItemClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        if (id == 2) {
            loading.setVisibility(View.VISIBLE);
            rvNowPlaying.setVisibility(View.INVISIBLE);
            Log.d(NowPlayingFragment.TAG, "OnCreateLoader Start");
            return new NowPlayingLoader(getContext());
        }
        Log.d(NowPlayingFragment.TAG, "OnCreateLoader Null");
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        MyNowPlayingAdapter adapter = (MyNowPlayingAdapter) rvNowPlaying.getAdapter();
        adapter.setMovies(data);
        adapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
        rvNowPlaying.setVisibility(View.VISIBLE);
        Log.d(NowPlayingFragment.TAG, "OnLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        MyNowPlayingAdapter adapter = (MyNowPlayingAdapter) rvNowPlaying.getAdapter();
        adapter.setMovies(null);
        Log.d(NowPlayingFragment.TAG, "onLoaderReset");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNowPlayingListFragmentItemClicked {
        // TODO: Update argument type and name
        void onNowPlayingListItemClicked(Movie item);
    }
}
