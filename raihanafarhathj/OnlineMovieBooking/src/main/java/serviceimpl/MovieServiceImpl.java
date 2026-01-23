package serviceimpl;

import service.MovieService;
import dao.MovieDAO;
import dao.impl.MovieDAOImpl;
import model.Movie;
import exception.NotFoundException;
import exception.ServiceException;
import enums.MovieStatus;

import java.util.List;
import java.util.stream.Collectors;

public class MovieServiceImpl implements MovieService {

    private final MovieDAO movieDAO;

    public MovieServiceImpl() {
        this.movieDAO = new MovieDAOImpl();
    }

    // ------------------- Admin Operations -------------------

    @Override
    public int addMovie(Movie movie) throws ServiceException {
        try {
            validateMovie(movie);
            return movieDAO.addMovie(movie);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add movie: " + ex.getMessage());
        }
    }

    @Override
    public boolean updateMovieStatus(int movieId, String status) throws ServiceException {
        try {
            MovieStatus.valueOf(status); // Validate enum
            List<Movie> movies = movieDAO.getMoviesByStatus("ALL"); // assuming DAO can handle ALL
            boolean exists = movies.stream().anyMatch(m -> m.getMovieId() == movieId);
            if (!exists) throw new ServiceException("Movie with ID " + movieId + " not found.");
            return movieDAO.getMovieById(movieId) != null; // You can replace with DAO update method
        } catch (IllegalArgumentException ex) {
            throw new ServiceException("Invalid movie status: " + status);
        } catch (Exception ex) {
            throw new ServiceException("Failed to update movie status: " + ex.getMessage());
        }
    }

    @Override
    public Movie getMovieById(int movieId) throws NotFoundException {
        try {
            Movie movie = movieDAO.getMovieById(movieId);
            if (movie == null) throw new NotFoundException("Movie with ID " + movieId + " not found.");
            return movie;
        } catch (Exception ex) {
            throw new NotFoundException("Error fetching movie: " + ex.getMessage());
        }
    }

    @Override
    public List<Movie> getMoviesByStatus(String status) throws ServiceException {
        try {
            return movieDAO.getMoviesByStatus(status)
                    .stream()
                    .filter(m -> m != null)
                    .sorted((m1, m2) -> m1.getMovieName().compareToIgnoreCase(m2.getMovieName()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching movies by status: " + ex.getMessage());
        }
    }

    // ------------------- User Operations -------------------

    @Override
    public List<Movie> getAllActiveMovies() throws ServiceException {
        try {
            return movieDAO.getMoviesByStatus(MovieStatus.NOW_SHOWING.name())
                    .stream()
                    .filter(m -> m != null)
                    .sorted((m1, m2) -> m1.getMovieName().compareToIgnoreCase(m2.getMovieName()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching active movies: " + ex.getMessage());
        }
    }

    // ------------------- Validation -------------------
    private void validateMovie(Movie movie) throws ServiceException {
        if (movie == null) throw new ServiceException("Movie cannot be null.");
        if (movie.getMovieName() == null || movie.getMovieName().isBlank())
            throw new ServiceException("Movie name cannot be empty.");
        if (movie.getLanguage() == null || movie.getLanguage().isBlank())
            throw new ServiceException("Movie language cannot be empty.");
        if (movie.getGenre() == null || movie.getGenre().isBlank())
            throw new ServiceException("Movie genre cannot be empty.");
        if (movie.getDuration() <= 0) throw new ServiceException("Movie duration must be positive.");
        if (movie.getRating() < 0.0 || movie.getRating() > 10.0)
            throw new ServiceException("Movie rating must be between 0 and 10.");
        if (movie.getAgeRating() == null) throw new ServiceException("Movie age rating must be specified.");
        if (movie.getStatus() == null) throw new ServiceException("Movie status must be specified.");
    }
}
