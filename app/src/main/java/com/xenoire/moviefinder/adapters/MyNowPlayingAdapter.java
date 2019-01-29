package com.xenoire.moviefinder.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.NowPlayingFragment;

import java.util.ArrayList;

public class MyNowPlayingAdapter extends RecyclerView.Adapter<MyNowPlayingAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    private final NowPlayingFragment.OnNowPlayingListFragmentItemClicked mListener;

    public MyNowPlayingAdapter(ArrayList<Movie> movies, NowPlayingFragment.OnNowPlayingListFragmentItemClicked listener, Context context) {
        this.movies = movies;
        this.mListener = listener;
        Context context1 = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mItem = movies.get(position);
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvOverview.setText(movies.get(position).getDescription());
        Picasso.get().load(movies.get(position).getPosterUrl()).into(holder.ivPoster);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onNowPlayingListItemClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        ImageView ivPoster;
        TextView tvTitle, tvOverview;
        public Movie mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivPoster = view.findViewById(R.id.iv_poster);
            tvOverview = view.findViewById(R.id.tv_movieShortOv);
            tvTitle = view.findViewById(R.id.tv_movieTitle);
            view.setTag(this);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + tvTitle.getText() + "'";
        }
    }
}
