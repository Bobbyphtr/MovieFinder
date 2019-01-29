package com.xenoire.moviefinderfavorite.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xenoire.moviefinderfavorite.OnFavoriteItemClicked;
import com.xenoire.moviefinderfavorite.R;
import com.xenoire.moviefinderfavorite.db.MovieItem;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Cursor movies;
    OnFavoriteItemClicked listener;

    public FavoriteAdapter(Cursor movies, OnFavoriteItemClicked listener, Context context) {
        this.movies = movies;
        this.listener = listener;
        Context context1 = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final MovieItem movie = getItem(i);
        viewHolder.movieItem = movie;
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        Picasso.get().load(movie.getPosterUrl()).into(viewHolder.ivPoster);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFavoriteListItemClicked(viewHolder.movieItem);
                }
            }
        });
    }

    private MovieItem getItem(int position) {
        if (!movies.moveToPosition(position)) {
            throw new IllegalStateException("Position is invalid");
        }
        return new MovieItem(movies);
    }

    @Override
    public int getItemCount() {
        if (movies == null) return 0;
        return movies.getCount();
    }

    public void setMovies(Cursor movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public MovieItem movieItem;
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
