package service;

import model.Theatre;
import exception.NotFoundException;
import exception.ServiceException;

import java.util.List;

public interface TheatreService {

    int addTheatre(Theatre theatre) throws ServiceException;
    boolean updateTheatreStatus(int theatreId, String status) throws ServiceException;
    Theatre getTheatreById(int theatreId) throws NotFoundException;
    List<Theatre> getTheatresByCity(int cityId) throws ServiceException;

    List<Theatre> getAllTheatres() throws ServiceException;
}
