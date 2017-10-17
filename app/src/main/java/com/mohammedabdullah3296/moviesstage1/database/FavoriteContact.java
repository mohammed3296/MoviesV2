package com.mohammedabdullah3296.moviesstage1.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mohammed Abdullah on 9/30/2017.
 */

public class FavoriteContact {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private FavoriteContact() {
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.mohammedabdullah3296.moviesstage1";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.favorites/favorites/ is a valid path for
     * looking at favorite data. content://com.example.android.favorites/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns {

        /**
         * The content URI to access the favorite data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVORITES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of favorites.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single favorite.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public final static String TABLE_NAME = "favorites";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FAVORITE_MOVIE_ID = "movie_id";
        public final static String ORIGINAL_TITLE = "original_title";
        public final static String POSTER_PATH = "poster_path";
        public final static String OVERVIEW = "overview";
        public final static String VOTE_AVERAGE = "vote_average";
        public final static String RELEASE_DATE = "release_date";
        public final static String BACKDROP_PATH = "backdrop_path";


    }

}
