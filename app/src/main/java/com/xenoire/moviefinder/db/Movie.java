package com.xenoire.moviefinder.db;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.ORIGINAL_ID;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.POSTER_URL;
import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.TITLE;
import static com.xenoire.moviefinder.db.DatabaseContract.getColumnInt;
import static com.xenoire.moviefinder.db.DatabaseContract.getColumnString;

public class Movie implements Parcelable {

    private String title, description, posterUrl, tagline;
    String lang, releaseDate, rating, duration;
    private ArrayList<String> genres;
    boolean isFavorite = false;
    private int id;

    //This Constructor is for search result only
    public Movie(JSONObject result) {
        try {
            this.id = result.getInt("id");
            this.title = result.getString("title");
            this.description = result.getString("overview");
            String posterUrl_temp = result.getString("poster_path");
            this.posterUrl = "https://image.tmdb.org/t/p/w185" + posterUrl_temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, ORIGINAL_ID);
        this.title = getColumnString(cursor, TITLE);
        this.description = getColumnString(cursor, OVERVIEW);
        this.posterUrl = getColumnString(cursor, POSTER_URL);
    }

    public Movie() {
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    //This method is to get the more details when clicked
    public void getDetails(JSONObject movieJSON) {
        try {
            this.id = movieJSON.getInt("id");
            this.title = movieJSON.getString("title");
            this.description = movieJSON.getString("overview");
            String lang_temp = movieJSON.getString("original_language");
            this.lang = lang_temp.toUpperCase();
            String posterUrl_temp = movieJSON.getString("poster_path");
            this.posterUrl = "https://image.tmdb.org/t/p/w185" + posterUrl_temp;
            this.releaseDate = movieJSON.getString("release_date");
            this.rating = movieJSON.getString("vote_average");
            this.duration = movieJSON.getString("runtime");
            this.tagline = movieJSON.getString("tagline");
            genres = new ArrayList<>();
            JSONArray genresArr = movieJSON.getJSONArray("genres");
            for (int i = 0; i < genresArr.length(); i++) {
                genres.add(genresArr.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.posterUrl);
        dest.writeString(this.lang);
        dest.writeString(this.releaseDate);
        dest.writeString(this.rating);
        dest.writeString(this.duration);
        dest.writeStringList(this.genres);
        dest.writeInt(this.id);
    }

    protected Movie(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.posterUrl = in.readString();
        this.lang = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readString();
        this.duration = in.readString();
        this.genres = in.createStringArrayList();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
