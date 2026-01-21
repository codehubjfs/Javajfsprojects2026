package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class will handle Guest data with database.
 * @author KavinT
 * @since 1.0
 */
public class GuestDAO {

    /**
     * This method will add new user to database.
     * @param userId
     * @param idProof
     * @throws Exception
     */
    public void addGuest(int userId, String idProof) throws Exception {
        String sql = "INSERT INTO guest (user_id, id_proof) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, idProof);
            ps.executeUpdate();
        }
    }

    /**
     * This method get guest detail with user id.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getGuestByUserId(int userId) throws Exception {

        String sql =
            "SELECT u.user_id, u.name, u.email, u.phone, u.gender, u.location_id, " +
            "u.created_at, u.updated_at, u.status, " +
            "g.guest_id, g.id_proof " +
            "FROM user u JOIN guest g ON u.user_id = g.user_id " +
            "WHERE u.user_id = ?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps.executeQuery();
    }
}
