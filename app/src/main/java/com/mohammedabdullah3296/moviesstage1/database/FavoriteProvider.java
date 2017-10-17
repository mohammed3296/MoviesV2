package com.mohammedabdullah3296.moviesstage1.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mohammed Abdullah on 9/30/2017.
 */

public class FavoriteProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = FavoriteProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int FAVORITES = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int FAVORITE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(FavoriteContact.CONTENT_AUTHORITY, FavoriteContact.PATH_FAVORITES, FAVORITES);

        // The content URI of the form "content://com.example.android.pets/pets/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.pets/pets/3" matches, but
        // "content://com.example.android.pets/pets" (without a number at the end) doesn't match.
        sUriMatcher.addURI(FavoriteContact.CONTENT_AUTHORITY, FavoriteContact.PATH_FAVORITES + "/#", FAVORITE_ID);
    }

    /**
     * Database helper object
     */
    private FavoriteDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoriteDbHelper(getContext());
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(FavoriteContact.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAVORITE_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FavoriteContact.FavoriteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FavoriteContact.FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return FavoriteContact.FavoriteEntry.CONTENT_LIST_TYPE;
            case FAVORITE_ID:
                return FavoriteContact.FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return insertFavorite(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertFavorite(Uri uri, ContentValues values) {
        try {
/*
            String movie_id = values.getAsString(FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID);
            String original_title = values.getAsString(FavoriteContact.FavoriteEntry.ORIGINAL_TITLE);
            String poster_path = values.getAsString(FavoriteContact.FavoriteEntry.POSTER_PATH);
            String overview = values.getAsString(FavoriteContact.FavoriteEntry.OVERVIEW);
            String vote_average = values.getAsString(FavoriteContact.FavoriteEntry.VOTE_AVERAGE);
            String release_date = values.getAsString(FavoriteContact.FavoriteEntry.RELEASE_DATE);
            String backdrop_path = values.getAsString(FavoriteContact.FavoriteEntry.BACKDROP_PATH);
*/
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            // Insert the new pet with the given values
            long id = database.insert(FavoriteContact.FavoriteEntry.TABLE_NAME, null, values);
            // If the ID is -1, then the insertion failed. Log an error and return null.
            if (id == -1) {
                Log.e(LOG_TAG, "Failed to insert row for " + uri);
                return null;
            }

            // Notify all listeners that the data has changed for the pet content URI
            getContext().getContentResolver().notifyChange(uri, null);

            // Return the new URI with the ID (of the newly inserted row) appended at the end
            return ContentUris.withAppendedId(uri, id);

        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, ">>>>>>>>>>>>>   " + e.getMessage().toString());
            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(FavoriteContact.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE_ID:
                // Delete a single row given by the ID in the URI
                selection = FavoriteContact.FavoriteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(FavoriteContact.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return updateFavorite(uri, contentValues, selection, selectionArgs);
            case FAVORITE_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = FavoriteContact.FavoriteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateFavorite(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateFavorite(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try {


            // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
            // check that the name value is not null.
            if (values.containsKey(FavoriteContact.FavoriteEntry.VOTE_AVERAGE)) {
                String vote_average = values.getAsString(FavoriteContact.FavoriteEntry.VOTE_AVERAGE);
                if (vote_average == null) {
                    throw new IllegalArgumentException("Movie requires a vote_average");
                }
            }

            // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
            // check that the gender value is valid.
            if (values.containsKey(FavoriteContact.FavoriteEntry.RELEASE_DATE)) {
                String release_date = values.getAsString(FavoriteContact.FavoriteEntry.RELEASE_DATE);
                if (release_date == null) {
                    throw new IllegalArgumentException("Movie requires valid release_date");
                }
            }

            // No need to check the breed, any value is valid (including null).

            // If there are no values to update, then don't try to update the database
            if (values.size() == 0) {
                return 0;
            }

            // Otherwise, get writeable database to update the data
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            // Perform the update on the database and get the number of rows affected
            int rowsUpdated = database.update(FavoriteContact.FavoriteEntry.TABLE_NAME, values, selection, selectionArgs);

            // If 1 or more rows were updated, then notify all listeners that the data at the
            // given URI has changed
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            // Return the number of rows updated
            return rowsUpdated;

        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

}
