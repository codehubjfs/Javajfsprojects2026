package com.vserv.dao.interfaces;

import java.sql.SQLException;

import com.vserv.model.Role;

public interface RoleDAO {
    Role findByName(String roleName) throws SQLException;
    Role findById(int roleId) throws SQLException;
}