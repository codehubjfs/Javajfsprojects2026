package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle Employee data with database.
 * @return
 * @throws Exception
 */
public class EmployeeDAO {

	/**
	 * This method will add new employee to database.
	 * @param userId
	 * @param roleId
	 * @param departmentId
	 * @param shift
	 * @param idProof
	 * @throws Exception
	 */
    public void addEmployee(int userId, int roleId,
                            int departmentId, String shift, String idProof) throws Exception {

        String sql = "INSERT INTO employee (user_id, role_id, department_id, shift, id_proof) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, roleId);
            ps.setInt(3, departmentId);
            ps.setString(4, shift);
            ps.setString(5, idProof);
            ps.executeUpdate();
        }
    }

    /**
     * This method will admin details from database.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getAdminByUserId(int userId) throws Exception {
        String sql = "SELECT u.user_id, u.name, u.email, u.phone, u.gender, u.location_id, u.status, u.created_at,\r\n"
        		+ "l.last_login, e.employee_id, e.role_id, e.department_id, e.shift, e.id_proof\r\n"
        		+ "FROM user u\r\n"
        		+ "JOIN employee e ON u.user_id = e.user_id\r\n"
        		+ "JOIN login l ON u.user_id = l.user_id\r\n"
        		+ "WHERE u.user_id = ? AND l.user_type = 'Admin'";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps.executeQuery();
    }

    /**
     * This method will employee details by user id.
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultSet getEmployeeByUserId(int userId) throws Exception {
        String sql = "SELECT u.user_id, u.name, u.email, u.phone, u.gender, u.location_id, u.status, u.created_at,\r\n"
        		+ "e.employee_id, e.role_id, e.department_id, e.shift, e.id_proof,\r\n"
        		+ "(SELECT last_login FROM login WHERE user_id=u.user_id) AS last_login\r\n"
        		+ "FROM user u\r\n"
        		+ "JOIN employee e ON u.user_id = e.user_id\r\n"
        		+ "WHERE u.user_id=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps.executeQuery();
    }

    /**
     * This method will get all employee from database.
     * @return
     * @throws Exception
     */
    public ResultSet getAllStaff() throws Exception {
        String sql = "SELECT u.name, u.email, u.phone, e.employee_id, e.role_id, e.department_id, e.shift\r\n"
        		+ "FROM user u\r\n"
        		+ "JOIN employee e ON u.user_id = e.user_id\r\n"
        		+ "ORDER BY e.employee_id ASC";

        Connection con = DBConnection.getConnection();
        return con.prepareStatement(sql).executeQuery();
    }
}
