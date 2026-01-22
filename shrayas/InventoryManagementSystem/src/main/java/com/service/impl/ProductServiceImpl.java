package com.service.impl;

import java.util.*;

import com.dao.CategoryDAO;
import com.dao.ProductDAO;
import com.dao.impl.ProductDAOImpl;
import com.exception.ApplicationException;
import com.model.Category;
import com.model.Product;
import com.service.ProductService;
import com.dao.impl.CategoryDAOImpl;

public class ProductServiceImpl implements ProductService{

	private final ProductDAO productDAO = new ProductDAOImpl();
    private final CategoryDAO categoryDAO = new CategoryDAOImpl();
    
	public int addProduct(Product product, String categoryName) throws Exception {
		
		if (product == null) {
	        throw new IllegalArgumentException("Product cannot be null");
	    }

	    if (categoryName == null) {
	        throw new IllegalArgumentException("Category name is required");
	    }

	    Category category = categoryDAO.findByName(categoryName);
	    if (category == null) {
	        throw new ApplicationException("Category does not exist");
	    }
	    int categoryId = category.getCategoryId();
	    product.setCategoryId(categoryId);

	    return productDAO.addProduct(product);
	}

    public Product getProductById(int productId) throws Exception {
    	
    	Product product = productDAO.findById(productId);
    	if (product == null) {
    		throw new ApplicationException("Product not found with id: " + productId);
    	}
    	
    	return product;
    }

    public List<Product> getProductsByCategory(int categoryId) throws Exception  {
    	
    	List<Product> products = productDAO.findByCategory(categoryId);
    	
    	if (products.isEmpty()) {
    		throw new ApplicationException("No products found for category id: " + categoryId);
    	}
    	
    	return products;
    }

    public List<Product> getAllProducts() throws Exception {
    	
    	List<Product> products = productDAO.findAll();
    	
    	if (products.isEmpty()) {
    	    throw new ApplicationException("No products available");
    	}

    	return products;
    }

    public boolean updateProduct(Product product) throws Exception {
    	
    	Product exist = productDAO.findById(product.getProductId());
    	
    	if (exist == null) {
    		 throw new ApplicationException("Cannot update. Product not found");
    	}
    	
    	boolean update = productDAO.update(product);
    	
    	if (!update) {
    		throw new ApplicationException("Product update failed");
    	}
    	
    	return true;
    }

    public boolean deleteProduct(int productId) throws Exception {
    	
    	Product exist = productDAO.findById(productId);
    	
    	if (exist == null) {
    		throw new ApplicationException("Cannot delete. Product not found");
    	}
    	
    	boolean delete = productDAO.delete(productId);
    	
    	if (!delete) {
    		throw new ApplicationException("Product deletion failed");
    	}
    	
    	return true;
    }

	@Override
	public int addProduct(Product product, Category name) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
