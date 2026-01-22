package com.dao;

import java.util.List;

import com.model.Product;

public interface ProductDAO {

	int addProduct(Product product) throws Exception;

    Product findById(int productId) throws Exception;

    List<Product> findByCategory(int categoryId) throws Exception;

    List<Product> findAll() throws Exception;

    boolean update(Product product) throws Exception;

    boolean delete(int productId) throws Exception;
}
