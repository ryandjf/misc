package com.junfengdai.service;

import com.junfengdai.dao.MovieDao;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MovieService2Test {

    @Test
    public void shouldVerifyReturn2() {
        MovieService service = new MovieService();
        MovieDao dao = mock(MovieDao.class);
        service.setMovieDao(dao);

        when(dao.countMovies()).thenReturn(3);
        //when(dao.countShows()).thenReturn(4);

        int number = service.retrieveMovieCount();
        assertThat(number, is(7));

        verify(dao).countMovies();
        verify(dao).countShows();
    }

    @Test
    public void shouldVerifyOrder(){
        List list = mock(List.class);

        list.add("one");
        list.clear();
        list.add("two");

        verify(list).clear();
        verify(list).add("two");
        verify(list).add("one");

    }
}
