package com.dao;

import java.util.List;

import com.model.Report;

public interface ReportDAO {
	
	int generateReport(String reportType) throws Exception;

    List<Report> findAll() throws Exception;
}
