package com.junfengdai.dao;

import com.junfengdai.domain.Movie;

public class MovieDao {
    public int countMovies() {
        return 2;
    }

    public int countShows() {
        return 1;
    }

    public Movie getMovieByName(String name){
        Movie movie = new Movie();
        movie.setDirector("John");
        movie.setName(name);
        return movie;
    }
}
