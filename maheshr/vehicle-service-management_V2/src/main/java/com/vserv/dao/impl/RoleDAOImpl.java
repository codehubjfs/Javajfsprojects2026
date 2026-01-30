package com.vserv.dao.impl;

import java.sql.*;

import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.RoleDAO;
import com.vserv.model.Role;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public Role findByName(String roleName) throws SQLException {
        String sql = "select * from role where role_name = ?";
        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        }
        return null;
    }

    @Override
    public Role findById(int roleId) throws SQLException {
        String sql = "select * from role where role_id = ?";
        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        }
        return null;
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleName(rs.getString("role_name"));
        role.setDescription(rs.getString("description"));
        return role;
    }
}
