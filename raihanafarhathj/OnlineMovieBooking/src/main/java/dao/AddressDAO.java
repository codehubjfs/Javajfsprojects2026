package dao;

import model.Address;
import exception.DataAccessException;
import java.util.List;

public interface AddressDAO {
    int addAddress(Address address) throws DataAccessException;
    Address getAddressById(int addressId) throws DataAccessException;
    List<Address> getAddressesByCity(int cityId) throws DataAccessException;
}
