package dao.impl;

import dao.RoleDAO;
import model.Role;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public Role getRoleById(int roleId) throws DataAccessException {
        String sql = "SELECT * FROM user_role WHERE role_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRole(rs);
                } else {
                    return null; // Or throw NotFoundException at service level
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching role by ID", e);
        }
    }

    @Override
    public Role getRoleByName(String roleName) throws DataAccessException {
        String sql = "SELECT * FROM user_role WHERE role_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roleName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRole(rs);
                } else {
                    return null; // Or throw NotFoundException at service level
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching role by name", e);
        }
    }

    @Override
    public List<Role> getAllRoles() throws DataAccessException {
        String sql = "SELECT * FROM user_role ORDER BY role_id";

        List<Role> roles = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }

            return roles;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching all roles", e);
        }
    }

    // --------- Helper method ---------
    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role(rs.getString("role_name"));
        role.setRoleId(rs.getInt("role_id")); // no setter issue here since DAO sets it internally
        return role;
    }
}
