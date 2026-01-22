package com.dao;

import java.util.List;

import com.model.StockTransaction;

public interface StockTransactionDAO {

	int save(StockTransaction transaction) throws Exception;

    List<StockTransaction> findByProductId(int productId) throws Exception;

    List<StockTransaction> findByUserId(int userId) throws Exception;
}
