package com.junfengdai.service;

import com.junfengdai.dao.MovieDao;
import com.junfengdai.domain.Movie;
import org.junit.Test;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MovieServiceTest {

    @Test
    public void shouldVerifyReturn() {
        MovieService service = new MovieService();
        MovieDao dao = createStrictMock(MovieDao.class);
        service.setMovieDao(dao);

        Movie expectedMovie = new Movie();
        expectedMovie.setName("Brad");
        //expect(dao.getMovieByName("Brad")).andReturn(expectedMovie);
        expect(dao.getMovieByName(eq("Brad"))).andReturn(expectedMovie);

        replay(dao);
        Movie movie = service.getMovieByName("Brad");
        verify(dao);

        assertThat(movie.getName(), is("Brad"));
    }


}
