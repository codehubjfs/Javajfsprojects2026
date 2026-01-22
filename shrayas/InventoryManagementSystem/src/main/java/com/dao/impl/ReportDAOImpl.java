package com.dao.impl;

import java.sql.*;
import java.util.*;

import com.dao.ReportDAO;
import com.model.Report;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class ReportDAOImpl implements ReportDAO{

	private static final String GENERATE = """
			INSERT INTO report (report_type) VALUE (?)
			""";
	
	private static final String FIND_ALL = """
			SELECT report_id, report_type, generated_at 
			FROM report
			""";
	
	public int generateReport(String reportType) throws Exception {
		
		try (Connection con = DatabaseConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(GENERATE)){
			
			ps.setString(1, reportType);
			
			return ps.executeUpdate();
		}
	}

    public List<Report> findAll() throws Exception {
    	
    	List<Report> reports = new ArrayList<>();
    	try (Connection con = DatabaseConnectionPool.getConnection();
    			PreparedStatement ps = con.prepareStatement(FIND_ALL);
    			ResultSet rs = ps.executeQuery()){
    		
    		while (rs.next()) {
    			reports.add(mapToReport(rs));
    		}
    	}
    	return reports;
    }
    
    private Report mapToReport(ResultSet rs) throws SQLException{
    	
    	Report report = new Report();
    	
    	report.setReportId(rs.getInt("report_id"));
    	report.setReportType(rs.getString("report_type"));
    	report.setGeneratedAt(DateUtil.toLocalDateTime(rs.getTimestamp("generated_at")));
    	
    	return report;
    }
}
