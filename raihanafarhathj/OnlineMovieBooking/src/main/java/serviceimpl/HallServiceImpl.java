package serviceimpl;

import service.HallService;
import dao.HallDAO;
import dao.impl.HallDAOImpl;
import model.Hall;
import exception.NotFoundException;
import exception.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

public class HallServiceImpl implements HallService {

    private final HallDAO hallDAO;

    public HallServiceImpl() {
        this.hallDAO = new HallDAOImpl();
    }

    @Override
    public int addHall(Hall hall) throws ServiceException {
        try {
            if (hall.getHallName() == null || hall.getHallName().isBlank()) {
                throw new ServiceException("Hall name cannot be empty.");
            }
            if (hall.getTotalSeats() <= 0) {
                throw new ServiceException("Total seats must be greater than 0.");
            }
            if (hall.getTheatreId() <= 0) {
                throw new ServiceException("Invalid theatre ID.");
            }

            return hallDAO.addHall(hall);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add hall: " + ex.getMessage());
        }
    }

    @Override
    public List<Hall> getHallsByTheatre(int theatreId) throws ServiceException {
        try {
            List<Hall> halls = hallDAO.getHallsByTheatre(theatreId);
            return halls.stream()
                        .filter(h -> h != null)
                        .sorted((h1, h2) -> h1.getHallName().compareToIgnoreCase(h2.getHallName()))
                        .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching halls for theatre: " + ex.getMessage());
        }
    }

    @Override
    public Hall getHallById(int hallId) throws NotFoundException {
        try {
            List<Hall> halls = hallDAO.getHallsByTheatre(0); // Temporary: fetch all to find by ID
            return halls.stream()
                        .filter(h -> h != null && h.getHallId() == hallId)
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("Hall with ID " + hallId + " not found."));
        } catch (Exception ex) {
            throw new NotFoundException("Error fetching hall: " + ex.getMessage());
        }
    }
}
