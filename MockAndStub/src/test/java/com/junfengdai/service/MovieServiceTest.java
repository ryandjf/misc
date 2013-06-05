package com.junfengdai.service;

import com.junfengdai.dao.MovieDao;
import org.junit.Test;

import static org.easymock.EasyMock.createStrictMock;
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

        expect(dao.countMovies()).andReturn(3);
        expect(dao.countShows()).andReturn(4);

        replay(dao);
        int number = service.retrieveMovieCount();
        verify(dao);
        assertThat(number, is(7));
    }


}
