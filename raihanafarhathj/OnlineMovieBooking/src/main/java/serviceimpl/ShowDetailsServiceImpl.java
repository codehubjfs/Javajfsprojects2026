package serviceimpl;

import service.ShowDetailsService;
import dao.ShowDetailsDAO;
import dao.impl.ShowDetailsDAOImpl;
import model.ShowDetails;
import exception.NotFoundException;
import exception.ServiceException;
import enums.ShowStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowDetailsServiceImpl implements ShowDetailsService {

    private final ShowDetailsDAO showDetailsDAO;

    public ShowDetailsServiceImpl() {
        this.showDetailsDAO = new ShowDetailsDAOImpl();
    }

    @Override
    public int addShow(ShowDetails show) throws ServiceException {
        validateShow(show);
        try {
            return showDetailsDAO.addShow(show);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add show: " + ex.getMessage());
        }
    }

    @Override
    public ShowDetails getShowById(int showId) throws NotFoundException, ServiceException {
        try {
            ShowDetails show = showDetailsDAO.getShowById(showId);
            if (show == null) {
                throw new NotFoundException("Show with ID " + showId + " not found.");
            }
            return show;
        } catch (Exception ex) {
            throw new ServiceException("Error fetching show: " + ex.getMessage());
        }
    }

    @Override
    public List<ShowDetails> getShowsByMovie(int movieId, LocalDate date) throws ServiceException {
        try {
            List<ShowDetails> shows = showDetailsDAO.getShowsByMovie(movieId, date);
            return shows.stream()
                    .filter(s -> s != null)
                    .sorted(Comparator.comparing(ShowDetails::getStartTime))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching shows for movie: " + ex.getMessage());
        }
    }

    @Override
    public List<ShowDetails> getShowsByHall(int hallId, LocalDate date) throws ServiceException {
        try {
            List<ShowDetails> shows = showDetailsDAO.getShowsByHall(hallId, date);
            return shows.stream()
                    .filter(s -> s != null)
                    .sorted(Comparator.comparing(ShowDetails::getStartTime))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching shows for hall: " + ex.getMessage());
        }
    }

    @Override
    public boolean updateShowStatus(int showId, String status) throws ServiceException {
        try {
            ShowStatus showStatus;
            try {
                showStatus = ShowStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new ServiceException("Invalid show status: " + status);
            }

            boolean updated = showDetailsDAO.updateShowStatus(showId, showStatus.name());
            if (!updated) {
                throw new ServiceException("Failed to update show status for show ID " + showId);
            }
            return true;
        } catch (Exception ex) {
            throw new ServiceException("Error updating show status: " + ex.getMessage());
        }
    }

    private void validateShow(ShowDetails show) throws ServiceException {
        if (show == null) {
            throw new ServiceException("Show details cannot be null.");
        }
        if (show.getMovieId() <= 0) {
            throw new ServiceException("Invalid movie ID.");
        }
        if (show.getHallId() <= 0) {
            throw new ServiceException("Invalid hall ID.");
        }
        if (show.getShowDate() == null) {
            throw new ServiceException("Show date cannot be null.");
        }
        if (show.getStartTime() == null || show.getEndTime() == null) {
            throw new ServiceException("Show start and end times cannot be null.");
        }
        if (show.getTicketPrice() <= 0) {
            throw new ServiceException("Ticket price must be positive.");
        }
        if (show.getEndTime().isBefore(show.getStartTime())) {
            throw new ServiceException("Show end time cannot be before start time.");
        }
        if (show.getShowStatus() == null) {
            throw new ServiceException("Show status cannot be null.");
        }
    }
}
