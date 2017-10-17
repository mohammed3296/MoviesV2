package com.mohammedabdullah3296.moviesstage1.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mohammedabdullah3296.moviesstage1.R;
import com.mohammedabdullah3296.moviesstage1.UTLS.URLS;
import com.mohammedabdullah3296.moviesstage1.database.FavoriteContact;
import com.mohammedabdullah3296.moviesstage1.reviewsData.Review;
import com.mohammedabdullah3296.moviesstage1.reviewsData.ReviewAdapter;
import com.mohammedabdullah3296.moviesstage1.reviewsData.ReviewQueryUtils;
import com.mohammedabdullah3296.moviesstage1.trailerData.Trailer;
import com.mohammedabdullah3296.moviesstage1.trailerData.TrailerAdapter;
import com.mohammedabdullah3296.moviesstage1.trailerData.TrailerQueryUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailesFavorite extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_FAVORITE_LOADER = 15864;

    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri mCurrentFavoriteUri;

    private TextView text_details_release_date;
    private TextView original_title;
    private TextView text_details_synopsis;
    private RatingBar mRate;
    private ImageView imageView;
    private ImageView favorite_image_detailes_favorites;

    private View TrailerloadingIndicator;
    private TextView TrailermEmptyStateTextView;
    private TrailerAdapter trailerAdapter;


    private View reviewloadingIndicator;
    private TextView reviewmEmptyStateTextView;
    private ReviewAdapter reviewAdapter;

    private String movieId;
    private String OriginalTitle;
    private String backdrop_path;
    private String vote_average;
    private String release_date;
    private String overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes_favorite);

        Intent intent = getIntent();
        mCurrentFavoriteUri = intent.getData();

        TrailerloadingIndicator = findViewById(R.id.loading_indicator_trailer_detailes_favorites);
        // Find a reference to the {@link ListView} in the layout
        ListView trailerListView = (ListView) findViewById(R.id.trailers_list_detailes_favorites);

        // Create a new adapter that takes an empty list of Movies as input
        trailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        trailerListView.setAdapter(trailerAdapter);

        TrailermEmptyStateTextView = (TextView) findViewById(R.id.empty_view_trailer_detailes_favorites);
        trailerListView.setEmptyView(TrailermEmptyStateTextView);

        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Trailer currentTrailer = trailerAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri trailerUri = Uri.parse(URLS.VIDIO_YOUTUBE + currentTrailer.getKey());

                // Create a new intent to view the Book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, trailerUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        reviewloadingIndicator = findViewById(R.id.loading_indicator_reviews_detailes_favorites);
        // Find a reference to the {@link ListView} in the layout
        ListView reviewsListView = (ListView) findViewById(R.id.reviews_list_detailes_favorites);

        // Create a new adapter that takes an empty list of Movies as input
        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewsListView.setAdapter(reviewAdapter);

        reviewmEmptyStateTextView = (TextView) findViewById(R.id.empty_view_reviews_detailes_favorites);
        reviewsListView.setEmptyView(reviewmEmptyStateTextView);
        reviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Review currentReview = reviewAdapter.getItem(i);
                Uri TrailerUri = Uri.parse(currentReview.getUrl());

                // Create a new intent to view the Book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, TrailerUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        text_details_release_date = (TextView) findViewById(R.id.text_details_favorites_release_date);
        original_title = (TextView) findViewById(R.id.original_title_detailes_favorite);
        text_details_synopsis = (TextView) findViewById(R.id.text_details_favorites_synopsis);
        mRate = (RatingBar) findViewById(R.id.rating_bar_detailes_favorites);
        imageView = (ImageView) findViewById(R.id.image_details_favorites_poster);
        favorite_image_detailes_favorites = (ImageView) findViewById(R.id.favorite_image_detailes_favorites);
        favorite_image_detailes_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorite_image_detailes_favorites.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                deleteFavorite();
            }
        });
        getLoaderManager().initLoader(EXISTING_FAVORITE_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                FavoriteContact.FavoriteEntry._ID,
                FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID,
                FavoriteContact.FavoriteEntry.ORIGINAL_TITLE,
                FavoriteContact.FavoriteEntry.POSTER_PATH,
                FavoriteContact.FavoriteEntry.OVERVIEW,
                FavoriteContact.FavoriteEntry.VOTE_AVERAGE,
                FavoriteContact.FavoriteEntry.RELEASE_DATE,
                FavoriteContact.FavoriteEntry.BACKDROP_PATH};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentFavoriteUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            // int _IDColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry._ID);
            int COLUMN_FAVORITE_MOVIE_IDColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID);
            int ORIGINAL_TITLEColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.ORIGINAL_TITLE);

            int OVERVIEWColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.OVERVIEW);
            int VOTE_AVERAGEColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.VOTE_AVERAGE);
            int RELEASE_DATEColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.RELEASE_DATE);
            int BACKDROP_PATHColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.BACKDROP_PATH);

            // Extract out the value from the Cursor for the given column index
            movieId = cursor.getString(COLUMN_FAVORITE_MOVIE_IDColumnIndex);
            Log.e("<><><><", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + movieId);
            OriginalTitle = cursor.getString(ORIGINAL_TITLEColumnIndex);
            backdrop_path = cursor.getString(BACKDROP_PATHColumnIndex);
            vote_average = cursor.getString(VOTE_AVERAGEColumnIndex);
            release_date = cursor.getString(RELEASE_DATEColumnIndex);
            overview = cursor.getString(OVERVIEWColumnIndex);

            // Update the views on the screen with the values from the database
            text_details_release_date.setText(release_date);
            original_title.setText(OriginalTitle);
            text_details_synopsis.setText(overview);
            mRate.setRating(Float.parseFloat(vote_average));
            Picasso.with(DetailesFavorite.this).load(URLS.IMAGE_SOURCE + backdrop_path).into(imageView);
            callTrailerAsyncTask();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        text_details_release_date.setText("");
        original_title.setText("");
        text_details_synopsis.setText("");
        mRate.setRating(0);
        imageView.setImageResource(0);
    }

    public void callTrailerAsyncTask() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {


            TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask();
            trailerAsyncTask.execute(URLS.MAIN_URL_FROM_DESCRIPTION + movieId + URLS.VEDIO_TERIAL);

            Log.e("?????????", movieId + "");
            ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask();
            reviewAsyncTask.execute(URLS.MAIN_URL_FROM_DESCRIPTION + movieId + URLS.VEDIO_REVIEW);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            TrailerloadingIndicator.setVisibility(View.GONE);

            TrailermEmptyStateTextView.setText(R.string.no_internet);

            reviewloadingIndicator.setVisibility(View.GONE);

            reviewmEmptyStateTextView.setText(R.string.no_internet);
        }
    }


    private class TrailerAsyncTask extends AsyncTask<String, Void, List<Trailer>> {
        @Override
        protected List<Trailer> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Trailer> result = TrailerQueryUtils.fetchTrailerData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Trailer> data) {
            TrailerloadingIndicator.setVisibility(View.GONE);
            TrailermEmptyStateTextView.setText("No Trailers Found");
            trailerAdapter.clear();
            if (data != null && !data.isEmpty()) {
                trailerAdapter.addAll(data);
            }
        }
    }


    private class ReviewAsyncTask extends AsyncTask<String, Void, List<Review>> {
        @Override
        protected List<Review> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Review> result = ReviewQueryUtils.fetchReviewData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Review> data) {
            reviewloadingIndicator.setVisibility(View.GONE);
            reviewmEmptyStateTextView.setText("No Reviews Found");
            reviewAdapter.clear();
            if (data != null && !data.isEmpty()) {
                reviewAdapter.addAll(data);
            }
        }
    }

    private void deleteFavorite() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentFavoriteUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentFavoriteUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_favorite_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_favorite_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
