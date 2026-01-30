package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.vserv.model.Catalog;

public interface CatalogDAO {
    List<Catalog> findAllServices() throws SQLException;
    List<Catalog> findByCarType(String carType) throws SQLException;
    Catalog findById(int catalogId) throws SQLException;
}