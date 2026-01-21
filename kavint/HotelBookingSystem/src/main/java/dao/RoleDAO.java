package dao;

import util.DBConnection;
import java.sql.*;

/**
 * This class will handle the role data with database.
 */
public class RoleDAO {

	/**
	 * This add new role to database.
	 * @param roleName
	 * @throws Exception
	 */
    public void addRole(String roleName) throws Exception {
        String sql = "INSERT INTO role VALUES (?)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, roleName);

        ps.executeUpdate();
        con.close();
    }
}
