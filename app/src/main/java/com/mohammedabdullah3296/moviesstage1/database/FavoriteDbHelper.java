package com.mohammedabdullah3296.moviesstage1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohammed Abdullah on 9/30/2017.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = FavoriteDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "favoritesDB.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link FavoriteDbHelper}.
     *
     * @param context of the app
     */
    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + FavoriteContact.FavoriteEntry.TABLE_NAME + " ("
                + FavoriteContact.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID + " TEXT NOT NULL  , "
                + FavoriteContact.FavoriteEntry.ORIGINAL_TITLE + " TEXT, "
                + FavoriteContact.FavoriteEntry.POSTER_PATH + " TEXT , "
                + FavoriteContact.FavoriteEntry.OVERVIEW + " TEXT , "
                + FavoriteContact.FavoriteEntry.VOTE_AVERAGE + " TEXT , "
                + FavoriteContact.FavoriteEntry.RELEASE_DATE + " TEXT , "
                + FavoriteContact.FavoriteEntry.BACKDROP_PATH + " TEXT);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
// The database is still at version 1, so there's nothing to do be done here.
    }
}
