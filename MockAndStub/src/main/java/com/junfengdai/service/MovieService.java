package com.junfengdai.service;

import com.junfengdai.dao.MovieDao;
import com.junfengdai.domain.Movie;

public class MovieService {
    private MovieDao movieDao;

    public void setMovieDao(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public int retrieveMovieCount() {
        return movieDao.countMovies() + movieDao.countShows();
        //return movieDao.countMovies() + 4;
        //return 9;
    }

    public Movie getMovieByName(String name) {
        return movieDao.getMovieByName(name);
    }
}
