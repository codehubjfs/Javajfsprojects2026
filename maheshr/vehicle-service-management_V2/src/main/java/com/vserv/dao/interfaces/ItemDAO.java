package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.ServiceItem;

public interface ItemDAO {
    List<ServiceItem> findByServiceId(int serviceId) throws SQLException;
    ServiceItem insert(ServiceItem item) throws SQLException;
    void delete(int itemId) throws SQLException;
}