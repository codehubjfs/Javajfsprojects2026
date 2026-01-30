package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.ServiceRecord;

public interface RecordDAO {
    List<ServiceRecord> findByAdvisorId(int advisorId) throws SQLException;
    List<ServiceRecord> findByStatus(String status) throws SQLException;
    ServiceRecord findById(int serviceId) throws SQLException;
    ServiceRecord findByBookingId(int bookingId) throws SQLException;
    ServiceRecord insert(ServiceRecord record) throws SQLException;
    void updateStatus(int serviceId, String status) throws SQLException;
    void updateRemarks(int serviceId, String remarks) throws SQLException;
    void complete(int serviceId) throws SQLException;
    void updateActualHours(int serviceId, double actualHours) throws SQLException;
}