package com.junfengdai.service;

import com.junfengdai.dao.MovieDao;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieService2Test {
    @Test
    public void shouldNotVerifyReturnValue() {
        MovieService service = new MovieService();
        MovieDao dao = mock(MovieDao.class);
        service.setMovieDao(dao);

        when(dao.countShows()).thenReturn(4);
        when(dao.countMovies()).thenReturn(5);

        int movieCount = service.retrieveMovieCount();

        assertThat(movieCount, is(9));

        verify(dao).countShows();
        verify(dao).countMovies();
        verify(dao).countMovies();
    }

    @Test
    public void test() {
        MarketEdge me = getMV();
        assertThat(me.value, is(50));
        assertThat(deliverBusinessValue(), is(30));
    }

    public int deliverBusinessValue() {
        int value = 10;

        try {
            value = 20;
            throw new RuntimeException();
        } catch (Exception e) {
            value = 30;
            return value;
        } finally {
            value = 50;
        }
    }

    private MarketEdge getMV() {
        MarketEdge marketEdge = new MarketEdge();

        try {
            marketEdge.value = 20;
            throw new RuntimeException();
        } catch (Exception e) {
            marketEdge.value = 30;
            return marketEdge;
        } finally {
            marketEdge.value = 50;
        }
    }
}
