package com.mohammedabdullah3296.moviesstage1.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

public class DetailsActivity extends AppCompatActivity {

    private TextView text_details_release_date;
    private TextView original_title;
    private TextView text_details_synopsis;
    private RatingBar mRate;
    private ImageView imageView;
    private ImageView favorite_image;

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
    private String poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TrailerloadingIndicator = findViewById(R.id.loading_indicator_trailer);
        // Find a reference to the {@link ListView} in the layout
        ListView trailerListView = (ListView) findViewById(R.id.trailers_list);

        // Create a new adapter that takes an empty list of Movies as input
        trailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        trailerListView.setAdapter(trailerAdapter);

        TrailermEmptyStateTextView = (TextView) findViewById(R.id.empty_view_trailer);
        trailerListView.setEmptyView(TrailermEmptyStateTextView);

        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Trailer currentTrailer = trailerAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri TrailerUri = Uri.parse(URLS.VIDIO_YOUTUBE + currentTrailer.getKey());

                // Create a new intent to view the Book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, TrailerUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        reviewloadingIndicator = findViewById(R.id.loading_indicator_reviews);
        // Find a reference to the {@link ListView} in the layout
        ListView reviewsListView = (ListView) findViewById(R.id.reviews_list);

        // Create a new adapter that takes an empty list of Movies as input
        reviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewsListView.setAdapter(reviewAdapter);

        reviewmEmptyStateTextView = (TextView) findViewById(R.id.empty_view_reviews);
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

        text_details_release_date = (TextView) findViewById(R.id.text_details_release_date);
        original_title = (TextView) findViewById(R.id.original_title);
        text_details_synopsis = (TextView) findViewById(R.id.text_details_synopsis);
        mRate = (RatingBar) findViewById(R.id.rating_bar);
        imageView = (ImageView) findViewById(R.id.image_details_poster);
        favorite_image = (ImageView) findViewById(R.id.favorite_image);

        favorite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Exists()) {
                    favorite_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    deleteFavorite();
                } else {
                    favorite_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                    saveFavorite();
                }
            }
        });
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("id")) {
            movieId = intentThatStartedThisActivity.getStringExtra("id");

        }
        if (intentThatStartedThisActivity.hasExtra("poster")) {
            poster = intentThatStartedThisActivity.getStringExtra("poster");

        }
        if (intentThatStartedThisActivity.hasExtra("OriginalTitle")) {
            OriginalTitle = intentThatStartedThisActivity.getStringExtra("OriginalTitle");

            original_title.setText(OriginalTitle);
        }

        if (intentThatStartedThisActivity.hasExtra("backdrop_path")) {
            backdrop_path = intentThatStartedThisActivity.getStringExtra("backdrop_path");
            Picasso.with(DetailsActivity.this).load(URLS.IMAGE_SOURCE + backdrop_path).into(imageView);
        }

        if (intentThatStartedThisActivity.hasExtra("vote_average")) {
            vote_average = intentThatStartedThisActivity.getStringExtra("vote_average");

            mRate.setRating(Float.parseFloat(vote_average));
        }

        if (intentThatStartedThisActivity.hasExtra("release_date")) {
            release_date = intentThatStartedThisActivity.getStringExtra("release_date");

            text_details_release_date.setText(release_date);
        }

        if (intentThatStartedThisActivity.hasExtra("overview")) {
            overview = intentThatStartedThisActivity.getStringExtra("overview");

            text_details_synopsis.setText(overview);
        }
        callTrailerAsyncTask();
        if (Exists()) {
            favorite_image.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }


    public void callTrailerAsyncTask() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {


            TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask();
            trailerAsyncTask.execute(URLS.MAIN_URL_FROM_DESCRIPTION + movieId + URLS.VEDIO_TERIAL);

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


    private void saveFavorite() {
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        Log.e(">>>>>", movieId);
        values.put(FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID, movieId);
        values.put(FavoriteContact.FavoriteEntry.ORIGINAL_TITLE, OriginalTitle);
        values.put(FavoriteContact.FavoriteEntry.BACKDROP_PATH, backdrop_path);
        values.put(FavoriteContact.FavoriteEntry.VOTE_AVERAGE, vote_average);
        values.put(FavoriteContact.FavoriteEntry.RELEASE_DATE, release_date);
        values.put(FavoriteContact.FavoriteEntry.OVERVIEW, overview);
        values.put(FavoriteContact.FavoriteEntry.POSTER_PATH, poster);

        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not

        // This is a NEW pet, so insert a new pet into the provider,
        // returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert(FavoriteContact.FavoriteEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Successful",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public boolean Exists() {
        String selection = FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID + " = ?";
        String[] selectionArgs = {movieId};
        Cursor count = getContentResolver().query(FavoriteContact.FavoriteEntry.CONTENT_URI, null, selection, selectionArgs, null);
        if (count == null || count.getCount() < 1) {
            return false;
        } else {
            return true;
        }
    }

    private void deleteFavorite() {
        String selection = FavoriteContact.FavoriteEntry.COLUMN_FAVORITE_MOVIE_ID + " = ?";
        String[] selectionArgs = {movieId};
        int rowsDeleted = getContentResolver().delete(FavoriteContact.FavoriteEntry.CONTENT_URI, selection, selectionArgs);

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
}
