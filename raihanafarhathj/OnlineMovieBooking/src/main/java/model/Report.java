package model;

import enums.ReportType;
import java.time.LocalDateTime;

public class Report {

    private int _reportId;
    private ReportType _reportType;
    private String _description;
    private LocalDateTime _generatedAt;
    private int _generatedBy;

    public Report() {}

    public Report(ReportType reportType, String description,
                  LocalDateTime generatedAt, int generatedBy) {
        this._reportType = reportType;
        this._description = description;
        this._generatedAt = generatedAt;
        this._generatedBy = generatedBy;
    }

    // Getters
    public int getReportId(){
    	return _reportId;
    }
    
    public ReportType getReportType(){
    	return _reportType; 
    }
    public String getDescription(){
    	return _description; 
    }
    public LocalDateTime getGeneratedAt(){
    	return _generatedAt; 
    }
    public int getGeneratedBy(){
    	return _generatedBy; 
    }

    // Setters
    public void setReportType(ReportType reportType){
    	this._reportType = reportType;
    }
    public void setDescription(String description){
    	this._description = description; 
    }
    public void setGeneratedAt(LocalDateTime generatedAt){
    	this._generatedAt = generatedAt;
    }
    public void setGeneratedBy(int generatedBy){
    	this._generatedBy = generatedBy; 
    }

    
    public String toString() {
        return "Report{" +
                "_reportId=" + _reportId +
                ", _reportType=" + _reportType +
                ", _description='" + _description + '\'' +
                ", _generatedAt=" + _generatedAt +
                ", _generatedBy=" + _generatedBy +
                '}';
    }
}
