package com.mohammedabdullah3296.moviesstage1.loders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.mohammedabdullah3296.moviesstage1.movieData.Movie;
import com.mohammedabdullah3296.moviesstage1.movieData.QueryUtils;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 10/1/2017.
 */


public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link MovieLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.w(LOG_TAG, "MovieLoader consrructor");
    }

    @Override
    protected void onStartLoading() {
        Log.w(LOG_TAG, "onStartLoading forceLoad");
        forceLoad();

    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Movie> loadInBackground() {
        Log.w(LOG_TAG, "loadInBackground loadInBackground");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Movies.
        List<Movie> Movies = QueryUtils.fetchMovieData(mUrl);
        return Movies;
    }
}
