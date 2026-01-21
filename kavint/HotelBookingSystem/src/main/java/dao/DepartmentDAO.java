package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle department data with database.
 * @author KavinT
 * @since 1.0
 */
public class DepartmentDAO {

	/**
	 * This method will add new department to database.
	 * @param departmentName
	 * @throws Exception
	 */
    public void addDepartment(String departmentName) throws Exception {
        String sql = "INSERT INTO department (department_name) VALUES (?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, departmentName);
            ps.executeUpdate();
        }
    }

    /**
     * This method will return all details of department.
     * @return
     * @throws Exception
     */
    public ResultSet getAllDepartments() throws Exception {
        String sql = "SELECT * FROM department ORDER BY department_id ASC";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
    }
}
