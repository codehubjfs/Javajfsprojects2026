package service;

import model.Movie;
import exception.NotFoundException;
import exception.ServiceException;

import java.util.List;

public interface MovieService {

    int addMovie(Movie movie) throws ServiceException;
    boolean updateMovieStatus(int movieId, String status) throws ServiceException;
    Movie getMovieById(int movieId) throws NotFoundException;
    List<Movie> getMoviesByStatus(String status) throws ServiceException;

    List<Movie> getAllActiveMovies() throws ServiceException;
}
