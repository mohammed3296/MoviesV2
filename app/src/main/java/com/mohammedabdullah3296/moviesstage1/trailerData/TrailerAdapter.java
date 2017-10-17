package com.mohammedabdullah3296.moviesstage1.trailerData;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohammedabdullah3296.moviesstage1.R;

import java.util.List;

/**
 * Created by Mohammed Abdullah on 10/1/2017.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Activity context, List<Trailer> trailers) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, trailers);
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
                    R.layout.trailer_list_item, parent, false);
        }
        // Get the {@link Movie} object located at this position in the list
        Trailer currentMovie = getItem(position);
        ImageView movieImage = (ImageView) listItemView.findViewById(R.id.trailer_list_item_icon);
        TextView name_trailer = (TextView) listItemView.findViewById(R.id.name_trailer);
        TextView trailer_site = (TextView) listItemView.findViewById(R.id.trailer_site);
        name_trailer.setText(currentMovie.getName());
        trailer_site.setText(currentMovie.getSite());
        return listItemView;
    }
}
