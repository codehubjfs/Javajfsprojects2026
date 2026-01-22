package com.service;

import com.model.Category;
import com.model.Product;
import java.util.*;

public interface ProductService {

	int addProduct(Product product, String categoryname) throws Exception;

    Product getProductById(int productId) throws Exception;

    List<Product> getProductsByCategory(int categoryId) throws Exception;

    List<Product> getAllProducts() throws Exception;

    boolean updateProduct(Product product) throws Exception;

    boolean deleteProduct(int productId) throws Exception;
}
