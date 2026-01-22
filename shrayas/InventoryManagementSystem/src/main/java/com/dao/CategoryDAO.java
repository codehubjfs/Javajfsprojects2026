package com.dao;

import java.util.List;

import com.model.Category;

public interface CategoryDAO {

	int createCategory(Category category) throws Exception;

    Category findById(int categoryId) throws Exception;
    
    Category findByName(String categoryName) throws Exception;

    List<Category> findAll() throws Exception;

    boolean update(Category category) throws Exception;

    boolean delete(int categoryId) throws Exception;
}
