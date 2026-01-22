package com.dao.impl;

import java.sql.*;

import com.dao.RoleDAO;
import com.util.DatabaseConnectionPool;

public class RoleDAOImpl implements RoleDAO{

	private static final String FIND_BY_TYPE = """
			SELECT role_id FROM role 
			WHERE role_name = ?
			""";
	
	public int getRoleIdByType(String roleType) throws Exception{
		
		try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE)){
			
			ps.setString(1, roleType);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return rs.getInt("role_id");
				}
			}
		}
			return 0;
	}
	
}
