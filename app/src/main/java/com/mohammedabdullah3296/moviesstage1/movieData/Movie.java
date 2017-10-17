package com.mohammedabdullah3296.moviesstage1.movieData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohammed Abdullah on 9/23/2017.
 */

public class Movie implements Parcelable {
    private String id;
    private String original_title;
    private String poster_path;
    private String overview;
    private double vote_average;
    private String release_date;
    private String backdrop_path;

    public Movie(String id, String original_title, String poster_path, String overview, double vote_average, String release_date, String backdrop_path) {
        this.id = id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.backdrop_path = backdrop_path;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
        backdrop_path = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeDouble(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(backdrop_path);
    }
}
