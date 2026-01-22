package com.model;

import java.time.LocalDateTime;

public class Report {

	private int reportId;
	private String reportType;
	private LocalDateTime generatedAt;
	
	public Report() {
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}

	public Report(String reportType) {
		this.reportType = reportType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public int getReportId() {
		return reportId;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}
	
}
