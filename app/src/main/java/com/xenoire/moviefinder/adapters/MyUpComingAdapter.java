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
import com.xenoire.moviefinder.fragments.UpComingFragment;

import java.util.ArrayList;

public class MyUpComingAdapter extends RecyclerView.Adapter<MyUpComingAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    private final UpComingFragment.OnUpComingListFragmentItemClicked listener;

    public MyUpComingAdapter(ArrayList<Movie> movies, UpComingFragment.OnUpComingListFragmentItemClicked listener, Context context) {
        this.movies = movies;
        this.listener = listener;
        Context context1 = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.movieItem = movies.get(position);
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvOverview.setText(movies.get(position).getDescription());
        Picasso.get().load(movies.get(position).getPosterUrl()).into(holder.ivPoster);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onUpComingListItemClicked(holder.movieItem);
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
        public final View view;
        public Movie movieItem;
        ImageView ivPoster;
        TextView tvTitle, tvOverview;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ivPoster = view.findViewById(R.id.iv_poster);
            tvTitle = view.findViewById(R.id.tv_movieTitle);
            tvOverview = view.findViewById(R.id.tv_movieShortOv);
            view.setTag(this);
        }
    }
}
