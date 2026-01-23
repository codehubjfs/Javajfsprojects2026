package service;

import model.ShowDetails;
import exception.NotFoundException;
import exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public interface ShowDetailsService {

    int addShow(ShowDetails show) throws ServiceException;

    ShowDetails getShowById(int showId) throws NotFoundException, ServiceException;

    List<ShowDetails> getShowsByMovie(int movieId, LocalDate date) throws ServiceException;

    List<ShowDetails> getShowsByHall(int hallId, LocalDate date) throws ServiceException;

    boolean updateShowStatus(int showId, String status) throws ServiceException;
}
