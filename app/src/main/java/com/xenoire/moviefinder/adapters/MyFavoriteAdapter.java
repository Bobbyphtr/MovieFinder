package com.xenoire.moviefinder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.FavoriteFragment;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder> {

    private Cursor movies;
    private final FavoriteFragment.OnFavoriteListFragmentItemClicked listener;
    private Context context;

    public MyFavoriteAdapter(Cursor movies, FavoriteFragment.OnFavoriteListFragmentItemClicked listener, Context context) {
        this.movies = movies;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Movie movie = getItem(position);
        holder.movieItem = movie;
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getDescription());
        Picasso.get().load(movie.getPosterUrl()).into(holder.ivPoster);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFavoriteListItemClicked(holder.movieItem);
                }
            }
        });
    }

    private Movie getItem(int position) {
        if (!movies.moveToPosition(position)) {
            throw new IllegalStateException("Position is invalid");
        }
        return new Movie(movies);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.getCount();
    }

    public void setMovies(Cursor movies) {
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
