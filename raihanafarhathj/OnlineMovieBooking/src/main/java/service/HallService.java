package service;

import model.Hall;
import exception.NotFoundException;
import exception.ServiceException;

import java.util.List;

public interface HallService {
    int addHall(Hall hall) throws ServiceException;
    List<Hall> getHallsByTheatre(int theatreId) throws ServiceException;
    Hall getHallById(int hallId) throws NotFoundException;
}
