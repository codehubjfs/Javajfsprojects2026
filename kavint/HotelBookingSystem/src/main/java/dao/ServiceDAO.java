package dao;

import util.DBConnection;

import java.sql.*;

/**
 * This class will handle the room service data with database.
 * @author KavinT
 * @since 1.0
 */
public class ServiceDAO {

	/**
	 * This method will add new service to the database.
	 * @param name
	 * @param price
	 * @throws Exception
	 */
    public void addService(String name, double price) throws Exception {
        String sql = "INSERT INTO service (service_name, service_price) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
        }
    }

    /**
     * This method will get all service from database.
     * @return
     * @throws Exception
     */
    public ResultSet getAllServices() throws Exception {
        String sql = "SELECT * FROM service ORDER BY service_id ASC";

        Connection con = DBConnection.getConnection();
        return con.prepareStatement(sql).executeQuery();
    }

    /**
     * This method will get service price by service id.
     * @param serviceId
     * @return
     * @throws Exception
     */
    public double getServicePrice(int serviceId) throws Exception {
        String sql = "SELECT service_price FROM service WHERE service_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }
    
    public void updateService(int id, String name, double price) throws Exception {
        String sql = "UPDATE service SET service_name=?, service_price=? WHERE service_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void deleteService(int id) throws Exception {
        String sql = "DELETE FROM service WHERE service_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
