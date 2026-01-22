package com.services;

import java.util.*;
import java.util.stream.Collectors;

import com.dao.*;
import com.modals.*;

public class FeedbackService {
	public FeedbackDAO feedDAO = new FeedbackDAO();
	
	public Map<Integer,List<Feedback>> getCustomerFeedback() throws Exception{
		Map<Integer,List<Feedback>> sortComment = feedDAO.viewFeedback().
				stream().collect(Collectors.groupingBy(Feedback::getCustomerID));
		
		return sortComment;
	}

}
