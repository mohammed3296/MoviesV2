package com.mohammedabdullah3296.moviesstage1.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohammedabdullah3296.moviesstage1.R;
import com.mohammedabdullah3296.moviesstage1.UTLS.URLS;
import com.mohammedabdullah3296.moviesstage1.loders.MovieLoader;
import com.mohammedabdullah3296.moviesstage1.movieData.Movie;
import com.mohammedabdullah3296.moviesstage1.movieData.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>  {
    private View loadingIndicator;
    private TextView mEmptyStateTextView;
    private MoviesAdapter mAdapter;
    public static final String LOG_TAG = MoviesActivity.class.getName();
    private static String url = URLS.POPULAR_MOVIES;
    private static final int MOVIE_LOADER_ID = 100000;
    private static final int MOVIE_LOADER_ID2 = 2895465;
    private GridView movieListView;

    private ArrayList<Movie> moviesArray;
    private ArrayList<Movie> saveMoviesArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        loadingIndicator = findViewById(R.id.loading_indicator);
        // Find a reference to the {@link ListView} in the layout
        movieListView = (GridView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of Movies as input
        moviesArray = new ArrayList<Movie>();
        mAdapter = new MoviesAdapter(this, moviesArray);
        movieListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        movieListView.setEmptyView(mEmptyStateTextView);

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current Movie that was clicked on
                Movie currentMovie = mAdapter.getItem(position);
                String id = currentMovie.getId();
                String poster = currentMovie.getPoster_path();
                String OriginalTitle = currentMovie.getOriginal_title();
                String backdrop_path = currentMovie.getBackdrop_path();
                String vote_average = String.valueOf(currentMovie.getVote_average());
                String release_date = currentMovie.getRelease_date();
                String overview = currentMovie.getOverview();
                Intent detailsIntent = new Intent(MoviesActivity.this, DetailsActivity.class);
                detailsIntent.putExtra("id", id);
                detailsIntent.putExtra("OriginalTitle", OriginalTitle);
                detailsIntent.putExtra("poster", poster);
                detailsIntent.putExtra("backdrop_path", backdrop_path);
                detailsIntent.putExtra("vote_average", vote_average);
                detailsIntent.putExtra("release_date", release_date);
                detailsIntent.putExtra("overview", overview);
                startActivity(detailsIntent);
            }
        });

        callMovieLoderAsyncTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            url = URLS.POPULAR_MOVIES;
            callMovieLoderAsyncTask();
            return true;
        } else if (id == R.id.action_top_rated) {
            url = URLS.TOP_RATED_MOVIES;
            callMovieLoderAsyncTask();
            return true;
        } else if (id == R.id.action_favorites) {
            Intent intent = new Intent(MoviesActivity.this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_movies);
        mAdapter.clear();
        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.w(LOG_TAG, "onLoaderReset");
// Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public void callMovieLoderAsyncTask() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            Log.w(LOG_TAG, "getLoaderManager");
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.w(LOG_TAG, "initLoader");

            if (url == URLS.POPULAR_MOVIES) {
                loaderManager.initLoader(MOVIE_LOADER_ID, null, this);

                Toast.makeText(this, "Popular Movies", Toast.LENGTH_SHORT).show();
            } else if (url == URLS.TOP_RATED_MOVIES) {
                loaderManager.initLoader(MOVIE_LOADER_ID2, null, this);
                Toast.makeText(this, "Top Rated Movies", Toast.LENGTH_SHORT).show();
            }

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveMoviesArray = moviesArray;
        outState.putParcelableArrayList("movieeeeees", saveMoviesArray);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter = new MoviesAdapter(this, savedInstanceState.<Movie>getParcelableArrayList("movieeeeees"));
    }

/**
 * this comments for some purposes , no relation between it and the app
 */
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        ConnectivityManager cm =
//                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//
//        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
//            //getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
//            if (url == URLS.POPULAR_MOVIES) {
//                getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
//
//            } else if (url == URLS.TOP_RATED_MOVIES) {
//                getLoaderManager().restartLoader(MOVIE_LOADER_ID2, null, this);
//
//            }
//        } else {
//            // Otherwise, display error
//            // First, hide loading indicator so error message will be visible
//            loadingIndicator.setVisibility(View.GONE);
//
//            // Update empty state with no connection error message
//            mEmptyStateTextView.setText(R.string.no_internet);
//        }
//    }


//    private class MovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {
//        @Override
//        protected List<Movie> doInBackground(String... urls) {
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            List<Movie> result = QueryUtils.fetchMovieData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Movie> data) {
//            loadingIndicator.setVisibility(View.GONE);
//            mEmptyStateTextView.setText(R.string.no_movies);
//            mAdapter.clear();
//            if (data != null && !data.isEmpty()) {
//                mAdapter.addAll(data);
//            }
//        }
//    }

}
