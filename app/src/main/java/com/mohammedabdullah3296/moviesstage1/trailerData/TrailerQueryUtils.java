package com.mohammedabdullah3296.moviesstage1.trailerData;

import android.text.TextUtils;
import android.util.Log;

import com.mohammedabdullah3296.moviesstage1.trailerData.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Abdullah on 10/1/2017.
 */

public final class TrailerQueryUtils {

    public static final String LOG_TAG = TrailerQueryUtils.class.getSimpleName();

    private TrailerQueryUtils() {
    }

    public static List<Trailer> fetchTrailerData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            Log.e(LOG_TAG, url.toString());
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create an {@link trailer} object
        List<Trailer> trailers = extractFeatureFromJson(jsonResponse);

        // Return the {@link trailer}
        return trailers;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            Log.e(LOG_TAG, "urlConnection = " + urlConnection.toString());
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the trailer JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link  } object by parsing out information
     * about the first trailer from the input MovtrailerieJSON string.
     */
    private static List<Trailer> extractFeatureFromJson(String TrailerJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(TrailerJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Trailer to
        List<Trailer> trailers = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(TrailerJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or Trailers).
            JSONArray TrailerArray = baseJsonResponse.getJSONArray("results");


            // For each Trailer in the TrailerArray, create an {@link Trailer} object
            for (int i = 0; i < TrailerArray.length(); i++) {

                // Get a single Trailer at position i within the list of Trailers
                JSONObject currentTrailer = TrailerArray.getJSONObject(i);
                String id = currentTrailer.getString("id");

                String iso_639_1 = currentTrailer.getString("iso_639_1");
                String iso_3166_1 = currentTrailer.getString("iso_3166_1");
                String key = currentTrailer.getString("key");
                String name = currentTrailer.getString("name");
                String site = currentTrailer.getString("site");
                String size = currentTrailer.getString("size");
                String type = currentTrailer.getString("type");

                Trailer trailer = new Trailer(id, iso_639_1, iso_3166_1, key, name, site, size, type);

                // Add the new {@link Trailer} to the list of Trailers.
                trailers.add(trailer);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the trailer JSON results", e);
        }

        // Return the list of Trailers
        return trailers;
    }
}
