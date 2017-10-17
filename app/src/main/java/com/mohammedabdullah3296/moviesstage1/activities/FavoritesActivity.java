package com.mohammedabdullah3296.moviesstage1.activities;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mohammedabdullah3296.moviesstage1.R;
import com.mohammedabdullah3296.moviesstage1.database.FavoriteContact;
import com.mohammedabdullah3296.moviesstage1.database.FavoriteCursorAdapter;

public class FavoritesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for the favorite data loader
     */
    private static final int FAVORITE_LOADER = 7874556;

    /**
     * Adapter for the ListView
     */
    FavoriteCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        // Find the ListView which will be populated with the favorite data
        GridView favoriteListView = (GridView) findViewById(R.id.favorites_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        favoriteListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of favorite data in the Cursor.
        // There is no favorite data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new FavoriteCursorAdapter(this, null);
        favoriteListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(FavoritesActivity.this, DetailesFavorite.class);

                // Form the content URI that represents the specific favorite that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link FavoriteEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.favorites/favorites/2"
                // if the favorite with ID 2 was clicked on.
                Uri currentFavoriteUri = ContentUris.withAppendedId(FavoriteContact.FavoriteEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentFavoriteUri);

                // Launch the {@link EditorActivity} to display the data for the current favorite.
                startActivity(intent);

                Log.e(">>>>>>>>>>>>>>>>>>>>>", Integer.toString((int) id));
                Log.e(">>>>>>>>>>>>>>>>>>>>>", currentFavoriteUri.toString());
            }
        });
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                FavoriteContact.FavoriteEntry._ID,
                FavoriteContact.FavoriteEntry.ORIGINAL_TITLE,
                FavoriteContact.FavoriteEntry.POSTER_PATH};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                FavoriteContact.FavoriteEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
