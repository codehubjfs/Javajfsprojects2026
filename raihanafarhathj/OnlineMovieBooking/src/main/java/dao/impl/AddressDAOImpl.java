package dao.impl;

import dao.AddressDAO;
import model.Address;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAOImpl implements AddressDAO {

    @Override
    public int addAddress(Address address) throws DataAccessException {
        String sql = "INSERT INTO address (street_name, area_name, landmark, pincode, city_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, address.getStreetName());
            ps.setString(2, address.getAreaName());
            ps.setString(3, address.getLandmark());
            ps.setInt(4, address.getPincode());
            ps.setInt(5, address.getCityId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);

                    // Set _addressId using reflection
                    java.lang.reflect.Field fieldId = Address.class.getDeclaredField("_addressId");
                    fieldId.setAccessible(true);
                    fieldId.set(address, generatedId);

                    return generatedId;
                }
            }

            throw new DataAccessException("Failed to generate address ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding address", e);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new DataAccessException("Reflection error while setting address ID", e);
        }
    }

    @Override
    public Address getAddressById(int addressId) throws DataAccessException {
        String sql = "SELECT * FROM address WHERE address_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, addressId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAddress(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching address by ID", e);
        }
    }

    @Override
    public List<Address> getAddressesByCity(int cityId) throws DataAccessException {
        String sql = "SELECT * FROM address WHERE city_id = ?";
        List<Address> addresses = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cityId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapResultSetToAddress(rs));
                }
            }

            return addresses;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching addresses by city", e);
        }
    }

    // ---------- Helper method ----------
    private Address mapResultSetToAddress(ResultSet rs) throws SQLException, DataAccessException {
        Address address = new Address();
        address.setStreetName(rs.getString("street_name"));
        address.setAreaName(rs.getString("area_name"));
        address.setLandmark(rs.getString("landmark"));
        address.setPincode(rs.getInt("pincode"));
        address.setCityId(rs.getInt("city_id"));

        try {
            // Set _addressId using reflection
            java.lang.reflect.Field fieldId = Address.class.getDeclaredField("_addressId");
            fieldId.setAccessible(true);
            fieldId.set(address, rs.getInt("address_id"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new DataAccessException("Reflection error while mapping address", e);
        }

        return address;
    }
}
