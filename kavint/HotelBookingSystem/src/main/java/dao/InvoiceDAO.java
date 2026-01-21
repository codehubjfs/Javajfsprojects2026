package dao;

import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class will handle the invoice data with database.
 * @author KavinT
 * @since 1.0
 */
public class InvoiceDAO {

	/**
	 * This will create new invoice for booking.
	 * @param date
	 * @param roomCharges
	 * @param serviceCharges
	 * @param tax
	 * @param total
	 * @param bookingId
	 * @throws Exception
	 */
    public void createInvoice(LocalDateTime date, double roomCharges, double serviceCharges,
                              double tax, double total, int bookingId) throws Exception {

        String sql = "INSERT INTO invoice (invoice_date, room_charges, service_charges, tax, total_amount, booking_id) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(date));
            ps.setDouble(2, roomCharges);
            ps.setDouble(3, serviceCharges);
            ps.setDouble(4, tax);
            ps.setDouble(5, total);
            ps.setInt(6, bookingId);
            ps.executeUpdate();
        }
    }

    /**
     * This method will get detail of invoice by booking id.
     * @param bookingId
     * @return
     * @throws Exception
     */
    public ResultSet getInvoicesByUser(int bookingId) throws Exception {
        String sql =
            "SELECT * FROM invoice " +
            "WHERE booking_id = ? ";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, bookingId);
        return ps.executeQuery();
    }
}
 