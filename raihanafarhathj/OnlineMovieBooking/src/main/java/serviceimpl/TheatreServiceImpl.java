package serviceimpl;

import service.TheatreService;
import dao.TheatreDAO;
import dao.impl.TheatreDAOImpl;
import model.Theatre;
import exception.NotFoundException;
import exception.ServiceException;
import enums.TheatreStatus;

import java.util.List;
import java.util.stream.Collectors;

public class TheatreServiceImpl implements TheatreService {

    private final TheatreDAO theatreDAO;

    public TheatreServiceImpl() {
        this.theatreDAO = new TheatreDAOImpl();
    }

    // ------------------- Admin Operations -------------------

    @Override
    public int addTheatre(Theatre theatre) throws ServiceException {
        try {
            validateTheatre(theatre);
            return theatreDAO.addTheatre(theatre);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add theatre: " + ex.getMessage());
        }
    }

    @Override
    public boolean updateTheatreStatus(int theatreId, String status) throws ServiceException {
        try {
            TheatreStatus.valueOf(status); // validate enum
            return theatreDAO.updateTheatreStatus(theatreId, status);
        } catch (IllegalArgumentException ex) {
            throw new ServiceException("Invalid theatre status: " + status);
        } catch (Exception ex) {
            throw new ServiceException("Failed to update theatre status: " + ex.getMessage());
        }
    }

    @Override
    public Theatre getTheatreById(int theatreId) throws NotFoundException {
        try {
            Theatre theatre = theatreDAO.getTheatreById(theatreId);
            if (theatre == null) throw new NotFoundException("Theatre with ID " + theatreId + " not found.");
            return theatre;
        } catch (Exception ex) {
            throw new NotFoundException("Error fetching theatre: " + ex.getMessage());
        }
    }

    @Override
    public List<Theatre> getTheatresByCity(int cityId) throws ServiceException {
        try {
            return theatreDAO.getTheatresByCity(cityId)
                    .stream()
                    .filter(t -> t != null)
                    .sorted((t1, t2) -> t1.getTheatreName().compareToIgnoreCase(t2.getTheatreName()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching theatres by city: " + ex.getMessage());
        }
    }

    // ------------------- Customer & Admin View -------------------

    @Override
    public List<Theatre> getAllTheatres() throws ServiceException {
        try {
            return theatreDAO.getTheatresByCity(0) // 0 or DAO method to fetch all theatres
                    .stream()
                    .filter(t -> t != null)
                    .sorted((t1, t2) -> t1.getTheatreName().compareToIgnoreCase(t2.getTheatreName()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching all theatres: " + ex.getMessage());
        }
    }

    // ------------------- Validation -------------------
    private void validateTheatre(Theatre theatre) throws ServiceException {
        if (theatre == null) throw new ServiceException("Theatre cannot be null.");
        if (theatre.getTheatreName() == null || theatre.getTheatreName().isBlank())
            throw new ServiceException("Theatre name cannot be empty.");
        if (theatre.getContactNumber() == null || theatre.getContactNumber().isBlank())
            throw new ServiceException("Contact number cannot be empty.");
    }
}
