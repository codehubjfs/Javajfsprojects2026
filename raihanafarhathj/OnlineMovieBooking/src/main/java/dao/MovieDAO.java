package dao;

import model.Movie;
import exception.DataAccessException;
import java.util.List;

public interface MovieDAO {
    int addMovie(Movie movie) throws DataAccessException;
    Movie getMovieById(int movieId) throws DataAccessException;
    List<Movie> getMoviesByStatus(String status) throws DataAccessException;
}
