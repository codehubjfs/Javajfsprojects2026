package com.recharge.service;

import java.util.List;

import com.recharge.dao.ConnectionDAO;

public class UserConnectionService {
	
	private final ConnectionDAO connectionDAO = new ConnectionDAO();
	
	/**
	 * used to add mobile number to the mobile_connection table
	 * @param userId
	 * @param mobile
	 * @param operatorName
	 * @param circle
	 */
	public void addMobileNumber(int userId, String mobile, String operatorName, String circle) {
		
		if(connectionDAO.mobileExists(mobile)) {
			throw new RuntimeException("Mobile number already exists");
		}
		
		int operatorId = connectionDAO.getOperatorIdByName(operatorName);
		connectionDAO.createConnection(mobile, operatorId, circle);

        System.out.println("Mobile number added successfully");
	}
	
	/**
	 * used to view their numbers
	 * @param userId
	 */
	public void viewMyNumbers(int userId) {

	    List<String> list = connectionDAO.getUserConnections(userId);

	    if (list.isEmpty()) {
	        System.out.println("No mobile numbers added yet");
	        return;
	    }

	    System.out.println("\n--- MY MOBILE NUMBERS ---");
	    System.out.printf("%-15s %-15s%n", "MOBILE NUMBER", "OPERATOR");
	    System.out.println("-------------------------------");

	    list.forEach(numbers -> {
	        String[] n = numbers.split("\\|");
	        System.out.printf("%-15s %-15s%n",
	                n[0].trim(),
	                n[1].trim()
	        );
	    });
	}


}
