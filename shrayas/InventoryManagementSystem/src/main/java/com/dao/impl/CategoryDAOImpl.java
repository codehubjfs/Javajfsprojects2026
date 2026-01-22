package com.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.model.Category;
import com.util.DatabaseConnectionPool;
import com.dao.CategoryDAO;
import com.enums.*;

public class CategoryDAOImpl implements CategoryDAO{

	private static final String INSERT ="""
			INSERT INTO category (name, description) VALUES (?, ?)
			""";
	
	private static final String FIND_BY_ID = """
			SELECT * FROM category WHERE category_id = ?
			"""; 
	
	private static final String FIND_BY_NAME = """
			SELECT * FROM category WHERE name = ?
			"""; 
	
	private static final String FIND_ALL = """
			SELECT category_id, name, description FROM category
            WHERE status = 'ACTIVE'
			"""; 
	
	private static final String UPDATE = """
			UPDATE category SET name = ?, description = ?
			WHERE category_id  = ? 
			""";
	
	private static final String INACTIVE_CATEGORY = """
			UPDATE category SET status = 'INACTIVE'
			WHERE category_id  = ?  
			""";
	public int createCategory(Category category) throws Exception {
		
		try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT)){
			
			ps.setString(1, category.getCategoryName());
			ps.setString(2, category.getDescription());
			return ps.executeUpdate();
		}
	}

    public Category findById(int categoryId) throws Exception {
    	
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_ID)){
			
			ps.setInt(1, categoryId);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapToCategory(rs);
				}
			}
		}
    	return null;
    }
    
    public Category findByName(String categoryName) throws Exception {
    	
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_NAME)){
			
			ps.setString(1, categoryName);
			
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapToCategory(rs);
				}
			}
		}

    	return null;
    }

    public List<Category> findAll() throws Exception {
    	
    	List<Category> categories = new ArrayList<>();
    	
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_ALL);
    			ResultSet rs = ps.executeQuery()){
    		
    		while (rs.next()) {
    			categories.add(mapToCategory(rs));
    		}
    	}

    	return categories;
    }

    public boolean update(Category category) throws Exception {
    	
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE)){
    		
    		ps.setString(1, category.getCategoryName());
    		ps.setString(2, category.getDescription());
    		ps.setInt(3, category.getCategoryId());
    		
    				return ps.executeUpdate() > 0;
    	}

    }

    public boolean delete(int categoryId) throws Exception {
    	
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(INACTIVE_CATEGORY)){
    		
    		ps.setInt(1, categoryId);
    		
    		return ps.executeUpdate() > 0;
    	}
    }
    
    private Category mapToCategory(ResultSet rs) throws SQLException {

        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setStatus(Status.valueOf(rs.getString("status")));
        
        return category;
    }
}
