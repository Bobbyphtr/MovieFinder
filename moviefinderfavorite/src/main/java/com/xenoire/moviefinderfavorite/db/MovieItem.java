package com.xenoire.moviefinderfavorite.db;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.xenoire.moviefinderfavorite.db.DatabaseContract.FavoriteMovieColumns.ORIGINAL_ID;
import static com.xenoire.moviefinderfavorite.db.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static com.xenoire.moviefinderfavorite.db.DatabaseContract.FavoriteMovieColumns.POSTER_URL;
import static com.xenoire.moviefinderfavorite.db.DatabaseContract.FavoriteMovieColumns.TITLE;
import static com.xenoire.moviefinderfavorite.db.DatabaseContract.getColumnInt;
import static com.xenoire.moviefinderfavorite.db.DatabaseContract.getColumnString;

public class MovieItem implements Parcelable {
    private int id;
    private String title, overview, posterUrl;

    public MovieItem(int id, String title, String overview, String posterUrl) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterUrl = posterUrl;
    }

    public MovieItem(Cursor cursor) {
        this.id = getColumnInt(cursor, ORIGINAL_ID);
        this.title = getColumnString(cursor, TITLE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.posterUrl = getColumnString(cursor, POSTER_URL);
    }

    public MovieItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.posterUrl);
    }

    protected MovieItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.posterUrl = in.readString();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
