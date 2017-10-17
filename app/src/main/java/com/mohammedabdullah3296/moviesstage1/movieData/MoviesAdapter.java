package com.mohammedabdullah3296.moviesstage1.movieData;

/**
 * Created by Mohammed Abdullah on 9/23/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mohammedabdullah3296.moviesstage1.R;
import com.mohammedabdullah3296.moviesstage1.UTLS.URLS;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 9/8/2017.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(Activity context, List<Movie> Movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, Movies);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }
        // Get the {@link Movie} object located at this position in the list
        Movie currentMovie = getItem(position);

        ImageView movieImage = (ImageView) listItemView.findViewById(R.id.movie_grid_item_image);
        Context context = getContext();
        // Picasso.with(dd).load(currentBook.getSmallThumbnailImage()).into(settingsDisplayImage);
        //Picasso.with(context).load(currentBook.getSmallThumbnailImage()).resize(50, 50).centerCrop().into(movieImage);
        Picasso.with(context).load(URLS.IMAGE_SOURCE + currentMovie.getPoster_path()).into(movieImage);
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
