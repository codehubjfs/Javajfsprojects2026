package dao;

import model.Report;
import exception.DataAccessException;
import java.util.List;

public interface ReportDAO {
    int generateReport(Report report) throws DataAccessException;
    List<Report> getReportsByType(String type) throws DataAccessException;
}
