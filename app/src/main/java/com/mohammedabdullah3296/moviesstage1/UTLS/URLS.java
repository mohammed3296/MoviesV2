package com.mohammedabdullah3296.moviesstage1.UTLS;

public class URLS {
    //the main url
    public static final String MAIN_URL_FROM_DESCRIPTION = "http://api.themoviedb.org/3/movie/";
    //To fetch trailers i  want to make a request to the /movie/{id}/videos endpoint.
    public static final String VEDIO_TERIAL = "/videos?api_key=0d3a607759ddd6f89e93d6228e834b3f";
    //To fetch reviews i want to make a request to the /movie/{id}/reviews endpoint
    public static final String VEDIO_REVIEW = "/reviews?api_key=0d3a607759ddd6f89e93d6228e834b3f";
    //The sort order can be by most popular, or by top rated
    public static final String POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular?api_key=0d3a607759ddd6f89e93d6228e834b3f";
    public static final String TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated?api_key=0d3a607759ddd6f89e93d6228e834b3f";
    //for Picasso
    public static final String IMAGE_SOURCE = "http://image.tmdb.org/t/p/w185/";
    public static final String VIDIO_YOUTUBE = "http://www.youtube.com/watch?v=" ;
}
