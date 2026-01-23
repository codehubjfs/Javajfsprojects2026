package service;

import model.ShowSeat;
import exception.ServiceException;

import java.util.List;

public interface ShowSeatService {
    int addShowSeat(ShowSeat showSeat) throws ServiceException;
    List<ShowSeat> getSeatsByShow(int showId) throws ServiceException;
    boolean updateSeatStatus(int showSeatId, String status) throws ServiceException;
}
