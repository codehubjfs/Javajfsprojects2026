package model;

import enums.SupportPriority;
import enums.SupportStatus;
import java.time.LocalDateTime;

public class CustomerSupport {

    private int _supportId; 
    private String _issueTitle;
    private String _issueDescription;
    private SupportPriority _priority;
    private SupportStatus _status;
    private LocalDateTime _createdAt;
    private int _userId;
    private Integer _assignedAdmin;
    

    public CustomerSupport() {}

    public CustomerSupport(String issueTitle, String issueDescription,
                           SupportPriority priority, SupportStatus status,
                           LocalDateTime createdAt, int userId) {
        this._issueTitle = issueTitle;
        this._issueDescription = issueDescription;
        this._priority = priority;
        this._status = status;
        this._createdAt = createdAt;
        this._userId = userId;
    }

    // Getters
    public int getSupportId(){
    	return _supportId; 
    }
    public String getIssueTitle(){
    	return _issueTitle; 
    }
    public String getIssueDescription(){
    	return _issueDescription; 
    }
    public SupportPriority getPriority(){
    	return _priority; 
    }
    public SupportStatus getStatus(){
    	return _status; 
    }
    public LocalDateTime getCreatedAt(){
    	return _createdAt; 
    }
    public int getUserId(){
    	return _userId; 
    }

    // Setters
    public void setIssueTitle(String issueTitle){
    	this._issueTitle = issueTitle; 
    }
    public void setIssueDescription(String issueDescription){
    	this._issueDescription = issueDescription; 
    }
    public void setPriority(SupportPriority priority){
    	this._priority = priority; 
    }
    public void setStatus(SupportStatus status){
    	this._status = status; 
    }
    public void setCreatedAt(LocalDateTime createdAt){
    	this._createdAt = createdAt; 
    }
    public void setUserId(int userId){
    	this._userId = userId; 
    }
    public Integer getAssignedAdmin() {
        return _assignedAdmin;
    }

    public void setAssignedAdmin(Integer assignedAdmin) {
        this._assignedAdmin = assignedAdmin;
    }


    @Override
    public String toString() {
        return "CustomerSupport{" +
                "_supportId=" + _supportId +
                ", _issueTitle='" + _issueTitle + '\'' +
                ", _issueDescription='" + _issueDescription + '\'' +
                ", _priority=" + _priority +
                ", _status=" + _status +
                ", _createdAt=" + _createdAt +
                ", _userId=" + _userId +
                '}';
    }
}
