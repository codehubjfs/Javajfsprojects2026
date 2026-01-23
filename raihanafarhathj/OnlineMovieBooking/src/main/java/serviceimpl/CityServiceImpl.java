package serviceimpl;

import service.CityService;
import dao.CityDAO;
import dao.impl.CityDAOImpl;
import model.City;
import exception.NotFoundException;
import exception.ServiceException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CityServiceImpl implements CityService {

    private final CityDAO cityDAO;

    public CityServiceImpl() {
        this.cityDAO = new CityDAOImpl();
    }

    @Override
    public int addCity(City city) throws ServiceException {
        try {
            if (city == null) {
                throw new ServiceException("City cannot be null.");
            }
            if (city.getCityName() == null || city.getCityName().isBlank()) {
                throw new ServiceException("City name cannot be empty.");
            }
            if (city.getState() == null || city.getState().isBlank()) {
                throw new ServiceException("State cannot be empty.");
            }

            return cityDAO.addCity(city);

        } catch (Exception ex) {
            throw new ServiceException("Failed to add city: " + ex.getMessage());
        }
    }

    @Override
    public City getCityById(int cityId) throws NotFoundException {
        try {
            if (cityId <= 0) {
                throw new NotFoundException("City ID must be greater than zero.");
            }

            City city = cityDAO.getCityById(cityId);
            if (city == null) {
                throw new NotFoundException("City with ID " + cityId + " not found.");
            }
            return city;

        } catch (Exception ex) {
            throw new NotFoundException("Error fetching city: " + ex.getMessage());
        }
    }

    @Override
    public List<City> getAllCities() throws ServiceException {
        try {
            List<City> cities = cityDAO.getAllCities();
            if (cities == null || cities.isEmpty()) {
                return List.of(); // empty list instead of null
            }

            // Sort cities lexicographically by city name
            return cities.stream()
                    .filter(Objects::nonNull)
                    .sorted((c1, c2) -> c1.getCityName().compareToIgnoreCase(c2.getCityName()))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            throw new ServiceException("Failed to fetch cities: " + ex.getMessage());
        }
    }
}
