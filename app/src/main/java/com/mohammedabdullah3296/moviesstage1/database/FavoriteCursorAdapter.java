package com.mohammedabdullah3296.moviesstage1.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohammedabdullah3296.moviesstage1.R;
import com.mohammedabdullah3296.moviesstage1.UTLS.URLS;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohammed Abdullah on 9/30/2017.
 */

public class FavoriteCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link FavoriteCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public FavoriteCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.favorite_list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView favorite_title = (TextView) view.findViewById(R.id.favorite_title);
        ImageView favorite_poster = (ImageView) view.findViewById(R.id.favorite_poster);

        // Find the columns of pet attributes that we're interested in
        int favorite_titleColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.ORIGINAL_TITLE);
        int favorite_posterColumnIndex = cursor.getColumnIndex(FavoriteContact.FavoriteEntry.POSTER_PATH);

        // Read the pet attributes from the Cursor for the current pet
        String favoriteName = cursor.getString(favorite_titleColumnIndex);
        String favorite_posterURL = cursor.getString(favorite_posterColumnIndex);

        // Update the TextViews with the attributes for the current pet
        favorite_title.setText(favoriteName);

        // Picasso.with(dd).load(currentBook.getSmallThumbnailImage()).into(settingsDisplayImage);
        //Picasso.with(context).load(currentBook.getSmallThumbnailImage()).resize(50, 50).centerCrop().into(movieImage);
        Picasso.with(context).load(URLS.IMAGE_SOURCE + favorite_posterURL).placeholder(R.drawable.noimage).into(favorite_poster);
        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView

    }
}