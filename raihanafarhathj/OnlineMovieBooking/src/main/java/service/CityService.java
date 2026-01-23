package service;

import model.City;
import exception.NotFoundException;
import exception.ServiceException;

import java.util.List;

public interface CityService {
    int addCity(City city) throws ServiceException;
    City getCityById(int cityId) throws NotFoundException;
    List<City> getAllCities() throws ServiceException;
}
