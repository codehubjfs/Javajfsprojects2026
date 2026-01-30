package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.vserv.model.Availability;

public interface AvailabilityDAO {
    List<Availability> findAll() throws SQLException;
    Availability findByDateAndSlot(LocalDate serviceDate, String timeSlot) throws SQLException;
    Availability insert(Availability slot) throws SQLException;
    void update(int availabilityId, int maxBookings, boolean isAvailable) throws SQLException;
    void delete(int availabilityId) throws SQLException;
}