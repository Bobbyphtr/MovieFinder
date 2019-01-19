package com.xenoire.moviefinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xenoire.moviefinder.MainActivity;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.DiscoverFragment;

import java.util.ArrayList;

public class MyDiscoverAdapter extends BaseAdapter {

    private ArrayList<Movie> movies = new ArrayList<>();
    private Context context;
    private DiscoverFragment.OnDiscoverItemClicked mListener;

    public MyDiscoverAdapter(ArrayList<Movie> movies, Context context, DiscoverFragment.OnDiscoverItemClicked mListener) {
        this.movies = movies;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        Log.d(MainActivity.TAG, "Movie list count : " + String.valueOf(movies.size()));
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
            Log.e(MainActivity.TAG, "LayoutInflated");
            holder = new ViewHolder();
            holder.ivPoster = convertView.findViewById(R.id.iv_poster);
            holder.tvOverview = convertView.findViewById(R.id.tv_movieShortOv);
            holder.tvTitle = convertView.findViewById(R.id.tv_movieTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(movies.get(position).getTitle());
        String overView = movies.get(position).getDescription().trim();
        holder.tvOverview.setText(overView);
        Picasso.get().load(movies.get(position).getPosterUrl()).into(holder.ivPoster);
        //Log.w(DiscoverFragment.TAG, "MyDiscoverAdapter : getView");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDiscoverItemClicked(movies.get(position));
                }
            }
        });

        return convertView;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvOverview;
    }
}
