package com.dao;

import com.model.Stock;

public interface StockDAO {

	int createStock(int productId, int quantity, int minThreshold) throws Exception;

    Stock findByProductId(int productId) throws Exception;

    boolean updateQuantity(int productId, int quantity) throws Exception;

    boolean updateThreshold(int productId, int minThreshold) throws Exception;
}
