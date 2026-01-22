package com.modals;

import java.time.*;

public class Feedback {
     private int _feedbackID;
     private String _comments;
     private int _rating;
     private String _busNo;
     private LocalDate _createdDate;
     private String _customerName;
     private int _customerID;
     
     public Feedback(int feedbackID,String comments,int rating,
    		 String busNo,LocalDate createdDate,String customerName,int customerID) {
    	 this._feedbackID = feedbackID;
    	 this._comments = comments;
    	 this._rating = rating;
    	 this._createdDate = createdDate;
    	 this._customerName = customerName;
    	 this._customerID = customerID;
     }
     
     public int getCustomerID() {return _customerID;}
     
     public String toString() {
    	 return "Customer Name : " + _customerName + " | Comments : " + _comments +
    			 " | Rating : " + _rating + " | Date : " + _createdDate;
     }
     
}
