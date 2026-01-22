package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.dao.ProductDAO;
import com.enums.Status;
import com.model.Product;
import com.util.DatabaseConnectionPool;

public class ProductDAOImpl implements ProductDAO{
	
	private static final String INSERT = """
			INSERT INTO product (product_name, description, price, category_idd)
			VALUES (?, ?, ?, ?)
			""";
	
	private static final String FIND_BY_ID = """
			SELECT * FROM product WHERE product_id = ?
			"""; 
	
	private static final String FIND_BY_CATEGORY = """
			SELECT * FROM product WHERE category_id = ?
			"""; 
	
	private static final String FIND_ALL = """
			SELECT * FROM product 
			"""; 
	
	private static final String UPDATE = """
			UPDATE product SET product_name = ?, description = ?, price = ?, category_id = ?
			WHERE product_id  = ? 
			""";
	
	private static final String INACTIVE_PRODUCT = """
			UPDATE product SET status = 'INACTIVE'
			WHERE product_id  = ?  
			""";
	public int addProduct(Product product) throws Exception{
		
		try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT)){
			
			ps.setString(1, product.getProductName());
			ps.setString(2, product.getDescription());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getCategoryId());
			
			return  ps.executeUpdate();
		}
		
	}

    public Product findById(int productId) throws Exception {
    	
    	try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_ID)){
			
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					return mapToProduct(rs);
				}
			}
    	}
    	return null;
    }

    public List<Product> findByCategory(int categoryId) throws Exception {
    	
    	List<Product> products = new ArrayList<>();
    	try(Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_BY_CATEGORY)){
    		
    		ps.setInt(1, categoryId);
    		
    		try (ResultSet rs = ps.executeQuery()){
    			while (rs.next()) {
    				products.add(mapToProduct(rs));
    			}
    		}
    	}
    	return products;
    }

    public List<Product> findAll() throws Exception {
    	
    	List<Product> products = new ArrayList<>();
    	
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_ALL);
    			ResultSet rs = ps.executeQuery()){
    		
    		while (rs.next()) {
    			products.add(mapToProduct(rs));
    		}
    	}
    	return products;
    }

    public boolean update(Product product) throws Exception {
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE)){
    		
    		ps.setString(1, product.getProductName());
			ps.setString(2, product.getDescription());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getCategoryId());
			ps.setInt(5, product.getProductId());
			
			return ps.executeUpdate() > 0;
    	}

    }

    public boolean delete(int productId) throws Exception {
    	try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(INACTIVE_PRODUCT)){
    		
    		ps.setInt(1, productId);
    		
    		return ps.executeUpdate() > 0;
    	}

    }
    
    private Product mapToProduct(ResultSet rs) throws SQLException {

        Product product = new Product();
        product.setCategoryId(rs.getInt("category_id"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setStatus(Status.valueOf(rs.getString("status")));
        product.setProductId(rs.getInt("product_id"));
        
        return product;
    }
}
