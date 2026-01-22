package com.dao;

import java.sql.*;
import com.util.*;
import java.util.*;
import com.modals.*;

public class FeedbackDAO {
     public List<Feedback> viewFeedback() throws Exception{
    	 List<Feedback> feedList = new ArrayList<>();
    	 String sql = "Select f.feedback_id as feed_id,u.user_name as customer_name,u.user_id as custID,"+
    			 "f.comments as feed_comments,"+
    	 		"f.rating as rate,f.bus_no as traveled_bus,"+
    	 		"f.created_date as comment_date from users u join "+
                "feedback f on u.user_id = f.customer_id ";
    	 try(Connection con = BusDBUtil.getConnection();Statement st = con.createStatement();
    			 ResultSet rs = st.executeQuery(sql)){
    		 while(rs.next()) {
    			 Feedback feed = new Feedback(
    					 rs.getInt("feed_id"),
    					 rs.getString("feed_comments"),
    					 rs.getInt("rate"),
    					 rs.getString("traveled_bus"),
    					 rs.getDate("comment_date").toLocalDate(),
    					 rs.getString("customer_name"),
    					 rs.getInt("custID")
    					 );
    			 feedList.add(feed);
    		 }
    	 }
    	 return feedList;
     }
}