package com.recharge.menu;


import com.recharge.exception.MobileNumberFormatException;
import com.recharge.model.User;
import com.recharge.service.AdminReadService;
import com.recharge.service.UserConnectionService;
import com.recharge.service.UserHistoryService;
import com.recharge.service.UserRechargeService;
import com.recharge.service.UserRefundRequestService;
import com.recharge.util.InputUtil;
import com.recharge.util.ValidationUtil;

public class UserMenu {

	private final User user;
	private final UserConnectionService connectionService = new UserConnectionService();
	private final AdminReadService readService = new AdminReadService();
	private final UserRechargeService rechargeService = new UserRechargeService();
	private final UserHistoryService historyService = new UserHistoryService();
	private final UserRefundRequestService refundRequestService = new UserRefundRequestService();
	
	public UserMenu(User user) {
		this.user = user;
	}
	
	public void start() {
		boolean running = true;
		
		while(running) {
                    
			System.out.println("\n=== USER DASHBOARD ===");
			System.out.println("Welcome, " + user.getFullName());
			System.out.println("""
			    1. Add Mobile Number
			    2. View My Mobile Numbers
			    3. Browse Plans
			    4. Recharge
			    5. View Recharge History
			    6. Request Refund
			    7. Logout
			    """);


            int choice = InputUtil.readInt("Choose option:", 1, 7);

            try {
	            switch(choice) {
	            	case 1 -> {
	            		String mobile;
	            		while(true) {
	            			mobile = InputUtil.readString("Mobile Number: ");
	            			try {
	            				ValidationUtil.isValidMobile(mobile);
	            				break;
	            			}
	            			catch(MobileNumberFormatException e) {
	            				System.out.println(e.getMessage());
	            			}
	            		}
	
	                    System.out.println("Available Operators:");
	                    readService.viewOperators();
	
	                    String operator = InputUtil.readString("Operator Name: ");
	                    
	                    String circle = InputUtil.readString("Circle (e.g. TN, KA, AP): ").toUpperCase();
	
	                    connectionService.addMobileNumber(user.getUserId(), mobile, operator, circle);
	            	}
	            	
	            	case 2 -> connectionService.viewMyNumbers(user.getUserId());
	            	
	            	case 3 -> readService.viewRechargePlans();
	            	
	            	case 4 -> {
	            		String mobile; 
	            		while(true) {
	            			mobile = InputUtil.readString("Mobile: ");
	            			try {
	            				ValidationUtil.isValidMobile(mobile);
	            				break;
	            			}
	            			catch(MobileNumberFormatException e) {
	            				System.out.println(e.getMessage());
	            			}
	            		}
	            		
	            		System.out.println("Available Plans: ");
	            		rechargeService.showAvailablePlans(mobile);
	            		
	            		String plan = InputUtil.readString("Enter Plan Name: ");
	            		String method = InputUtil.readString("Payment Method (UPI/CARD)");
	            	
	            		rechargeService.recharge(user.getUserId(), mobile, plan, method);
	            	}
	            	
	            	case 5 -> historyService.viewHistory(user.getUserId());
	            	
	            	case 6 -> {
	            		String txnRef = InputUtil.readString("Enter Transaction Reference: ");
	            		refundRequestService.requestRefund(txnRef);
	            	}
	            	
	            	case 7 -> {
	            		running = false;
	            		System.out.println(user.getFullName()+" Logged Out");
	            	}
	            }
            }
            catch(Exception e) {
            	System.out.println(e.getMessage());
            }
		}
	}
}
