package com.xenoire.moviefinder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.xenoire.moviefinder.db.DatabaseContract.FavoriteMovieColumns.ORIGINAL_ID;
import static com.xenoire.moviefinder.db.DatabaseContract.TABLE_NAME;

public class FavoriteMovieHelper {

    public static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    private static final String TAG = FavoriteMovieHelper.class.getSimpleName();

    public FavoriteMovieHelper(Context context) {
        this.context = context;
    }

    public FavoriteMovieHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

//    public ArrayList<Movie> query(){
//        ArrayList<Movie> arrayList = new ArrayList<>();
//        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " DESC", null);
//        cursor.moveToFirst();
//        Movie movie;
//        if (cursor.getCount() > 0) {
//            do {
//                movie = new Movie();
//                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
//                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
//                movie.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
//                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ORIGINAL_ID)));
//                arrayList.add(movie);
//                cursor.moveToNext();
//            } while (!cursor.isAfterLast());
//        }
//        cursor.close();
//        Log.d(TAG, "    Query()");
//        return arrayList;
//    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ORIGINAL_ID + " = ?", new String[]{id});
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                ORIGINAL_ID + " DESC");
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null,
                ORIGINAL_ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(ContentValues values, String id) {
        return database.update(DATABASE_TABLE, values, ORIGINAL_ID + " = ?", new String[]{id});
    }
}
