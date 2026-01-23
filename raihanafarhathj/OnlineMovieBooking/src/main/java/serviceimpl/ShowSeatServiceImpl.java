package serviceimpl;

import service.ShowSeatService;
import dao.ShowSeatDAO;
import dao.impl.ShowSeatDAOImpl;
import model.ShowSeat;
import exception.DataAccessException;
import exception.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowSeatDAO showSeatDAO;

    public ShowSeatServiceImpl() {
        this.showSeatDAO = new ShowSeatDAOImpl();
    }

    @Override
    public int addShowSeat(ShowSeat showSeat) throws ServiceException {
        try {
            if (showSeat == null) {
                throw new ServiceException("Cannot add null ShowSeat.");
            }
            return showSeatDAO.addShowSeat(showSeat);
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to add ShowSeat: " + ex.getMessage());
        }
    }

    @Override
    public List<ShowSeat> getSeatsByShow(int showId) throws ServiceException {
        try {
            List<ShowSeat> seats = showSeatDAO.getSeatsByShow(showId);
            return seats.stream()
                        .filter(s -> s != null)
                        .sorted((s1, s2) -> Integer.compare(s1.getSeatId(), s2.getSeatId()))
                        .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to fetch seats for show ID " + showId + ": " + ex.getMessage());
        }
    }

    @Override
    public boolean updateSeatStatus(int showSeatId, String status) throws ServiceException {
        try {
            if (status == null || status.isBlank()) {
                throw new ServiceException("Seat status cannot be empty.");
            }
            return showSeatDAO.updateSeatStatus(showSeatId, status);
        } catch (DataAccessException ex) {
            throw new ServiceException("Failed to update seat status: " + ex.getMessage());
        }
    }
}
