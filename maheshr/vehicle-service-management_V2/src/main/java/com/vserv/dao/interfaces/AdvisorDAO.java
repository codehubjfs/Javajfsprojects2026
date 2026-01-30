package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.Advisor;

public interface AdvisorDAO {
    List<Advisor> findAll() throws SQLException;
    List<Advisor> findAvailable() throws SQLException;
    Advisor findById(int advisorId) throws SQLException;
    void updateAvailability(int advisorId, String status) throws SQLException;
    void updateLoad(int advisorId, int loadChange) throws SQLException;
    void insert(Advisor advisor) throws SQLException;
    void updateAdvisorStatus(int advisorId, String status) throws SQLException;
}