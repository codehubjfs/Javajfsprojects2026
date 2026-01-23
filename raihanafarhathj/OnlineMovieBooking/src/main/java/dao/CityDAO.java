package dao;

import model.City;
import exception.DataAccessException;
import java.util.List;

public interface CityDAO {
    int addCity(City city) throws DataAccessException;
    City getCityById(int cityId) throws DataAccessException;
    List<City> getAllCities() throws DataAccessException;
}
