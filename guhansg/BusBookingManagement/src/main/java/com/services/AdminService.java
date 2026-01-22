package com.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.dao.FeedbackDAO;
import com.dao.RouteDAO;
import com.dao.UserDAO;
import com.exception.InvalidEmailException;
import com.exception.InvalidMobileException;
import com.exception.InvalidNameException;
import com.exception.InvalidNumberFormat;
import com.exception.InvalidPasswordException;
import com.util.ValidationUtil;

public class AdminService {
	public static void manageRoute() throws Exception{
		 Scanner sc = new Scanner(System.in);
		 RouteDAO route = new RouteDAO();
		 boolean adminFuncLoop = true;
		 while(adminFuncLoop) {
			 System.out.println("1.Add New Route\n"+
					 "2.Remove Route\n"+
					 "3.View All Available Route+"+
					 "4.Exit\n"
			 );
			 int manageRouteOption;
				while(true) {
					try {
						System.out.print("Enter Your Option : ");
						manageRouteOption = ValidationUtil.menuOptionCheck(sc.nextLine());
						break;
					}catch(InvalidNumberFormat e) {
						System.out.println(e.getMessage());
					}
				}
			 
			 switch(manageRouteOption) {
			 case 1:	 
				  System.out.print("Enter source : ");
				  String source = sc.nextLine();
				  System.out.print("Enter destination : ");
				  String destination = sc.nextLine();
				  System.out.print("Enter Distance : ");
				  int distance = sc.nextInt();
				  String time;
				  while(true) {
					  try {
						  System.out.print("Enter estimated time (HH-mm) :");
		    			  sc.nextLine();
		    			  time = sc.nextLine();
		    			  ValidationUtil.parseTime(time);
		    			  break;
					  }catch(Exception e) {
						 System.out.println("Invalid time format, Please provide given format"); 
					  }
				  }
				  
				  DateTimeFormatter format = DateTimeFormatter.ofPattern("HH-mm");
				  LocalTime estimateTime = LocalTime.parse(time,format);
				  
				  route.addNewRoute(source,destination,distance,estimateTime);
				  System.out.println("Route has been added successfully");
	             System.out.println("======================================");
	             break;
				  
			 case 2:
				  
				  System.out.print("Enter source : ");
				  String removeSource = sc.nextLine();
				  System.out.print("Enter destination : ");
				  String removeDestination = sc.nextLine();
				  
				  route.removeExistingRoute(removeSource, removeDestination);
				  System.out.println("Route has been removed successfully");
	              System.out.println("======================================");
	              
				  break;
				  
			 case 3:
				 System.out.println("All Available Routes -> ");
				 System.out.println(RouteService.getAllRoute());
				 System.out.println("======================================");
				 break;
				 
			 case 4:
				 adminFuncLoop = false;
				 break;
				 
			default:
			    System.out.println("Invalid route option");
				  
			 }
		 }
		 
	}
	
	public static void manageOperator() throws Exception{
		Scanner sc = new Scanner(System.in);
		UserDAO operator = new UserDAO();
		
		boolean adminFuncLoop1 = true;
		while(adminFuncLoop1) {
			System.out.println("1.Add New Operator\n"+
					 "2.Remove Operator\n"+
					 "3.View All Available Operator\n"+
					 "4.Exit\n"
			 );
			int manageOperatorOption;
			while(true) {
				try {
					System.out.print("Enter Your Option : ");
					manageOperatorOption = ValidationUtil.menuOptionCheck(sc.nextLine());
					break;
				}catch(InvalidNumberFormat e) {
					System.out.println(e.getMessage());
				}
			}
			 switch(manageOperatorOption) {
			 case 1:	  
				  String opName;String opMobile;String opEmail;
				  String opPassword;String opDate;
				  while(true) {
						try {
							System.out.print("Enter Operator Name : ");
							opName = sc.nextLine();
							ValidationUtil.checkName(opName);
							break;
						}catch(InvalidNameException e) {
							System.out.println(e.getMessage());
						}
				  }
				  while(true) {
						try {
							System.out.print("Enter Operator Email : ");
							opEmail = sc.nextLine();
							ValidationUtil.checkEmail(opEmail);
							break;
						}catch(InvalidEmailException e) {
							System.out.println(e.getMessage());
						}
			     }
				 while(true) {
					   try {
							System.out.print("Enter Operator password : ");
							opPassword = sc.nextLine();
							ValidationUtil.checkPassword(opPassword);
							break;
						}catch(InvalidPasswordException e) {
							System.out.println(e.getMessage());
						}
						
				  }
				  while(true) {
						try {
							System.out.print("Enter Operator mobile : ");
							opMobile = sc.nextLine();
							ValidationUtil.checkMobile(opMobile);
							break;
						}catch(InvalidMobileException e) {
							System.out.println(e.getMessage());
						}
				  }
				  while(true) {
					  try {
						  System.out.print("Enter the created Date (yyyy-MM-dd) : ");
	                     opDate = sc.nextLine();
	                     ValidationUtil.parseDate(opDate);
	                     break;
					  }catch(Exception e) {
						  System.out.println(e.getMessage());
					  }
				  }
	             
	             DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	             LocalDate opFormatDate = LocalDate.parse(opDate,format1);

	             operator.addNewOperator(opName,opMobile,opEmail,opPassword,opFormatDate);
	             System.out.println(opName+ " has been added successfully");
	             System.out.println("======================================");
	             break;
			 case 2:
	             System.out.print("Enter Operator Name to remove: ");
	             String delOpName = sc.nextLine();
	             System.out.print("Enter Operator Number to remove: ");
	             String delOpMobile = sc.nextLine();

	             operator.removeExistigOperator(delOpName,delOpMobile);
	             System.out.println(delOpName + " has been removed successfully");
	             System.out.println("======================================");
	             break;

	        case 3:
	             System.out.println("List of Operator:");
	             operator.viewAllAvailableOperator();
	             System.out.println("======================================");
	             break;
	             
	        case 4:
	        	adminFuncLoop1 = false;
	        	break;
	        	
	        default : 
	        	System.out.println("Please enter valid option");
			 }
		}
		
		
		}
		 
	public static void viewFeedback() throws Exception{
		System.out.println("Customer's Feedback");
		FeedbackService feedService = new FeedbackService();
		System.out.println(feedService.getCustomerFeedback());
        System.out.println("==============================================");
	}
	

}
