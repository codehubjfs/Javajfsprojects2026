package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.Feedback;

public interface FeedbackDAO {
    List<Feedback> findByCustomerId(int customerId) throws SQLException;
    Feedback findByServiceId(int serviceId) throws SQLException;
    Feedback insert(Feedback feedback) throws SQLException;
}