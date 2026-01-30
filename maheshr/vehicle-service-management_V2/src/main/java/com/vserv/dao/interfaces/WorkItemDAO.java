package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.WorkItem;

public interface WorkItemDAO {
    List<WorkItem> findAll() throws SQLException;
    List<WorkItem> findByCarType(String carType) throws SQLException;
    WorkItem findById(int workItemId) throws SQLException;
}