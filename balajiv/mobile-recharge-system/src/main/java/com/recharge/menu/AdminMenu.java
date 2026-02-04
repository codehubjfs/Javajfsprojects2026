package com.recharge.menu;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.recharge.model.Offer;
import com.recharge.model.RechargePlan;
import com.recharge.model.User;
import com.recharge.service.*;
import com.recharge.util.InputUtil;


public class AdminMenu {
	
	private final User admin;
	private final AdminReadService readService = new AdminReadService();
	private final AdminOperatorService operatorService = new AdminOperatorService();
	private final AdminRechargePlanService planService = new AdminRechargePlanService();
	private final AdminOfferService offerService = new AdminOfferService();
	private final AdminRefundService refundService = new AdminRefundService();
	
	
	public AdminMenu(User admin) {
		this.admin = admin;
	}
	
	public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("Welcome, " + admin.getFullName());
            System.out.println("""
                1. View / Reports
                2. Operator Management
                3. Recharge Plan Management
                4. Offer Management
                5. Offer–Plan Mapping
                6. Refund Management
                7. Logout
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 7);

            try {
                switch (choice) {
                    case 1 -> viewReports();
                    case 2 -> operatorManagement();
                    case 3 -> planManagement();
                    case 4 -> offerManagement();
                    case 5 -> offerPlanMapping();
                    case 6 -> refundManagement();
                    case 7 -> running = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
	
	/* =============== VIEW / REPORTS ==================== */
	
	private void viewReports() {
        boolean back = false;

        while (!back) {
            System.out.println("""
            		
                === VIEW / REPORTS ===
                1. View Operators
                2. View Recharge Plans
                3. View Recharge Transactions
                4. View Payments
                5. View Invoices
                6. Back
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 6);

            switch (choice) {
                case 1 -> readService.viewOperators();
                case 2 -> readService.viewRechargePlans();
                case 3 -> readService.viewRechargeTransactions();
                case 4 -> readService.viewPayments();
                case 5 -> readService.viewInvoices();
                case 6 -> back = true;
            }
        }
    }
	
	/* ================= OPERATOR MANAGEMENT ================= */

	private void operatorManagement() {
	    boolean back = false;

	    while (!back) {
	        System.out.println("""
	                
	            === OPERATOR MANAGEMENT ===
	            1. Add Operator
	            2. Activate Operator
	            3. Deactivate Operator
	            4. Back
	            """);

	        int choice = InputUtil.readInt("Choose option:", 1, 4);

	        try {
	            switch (choice) {

	                case 1 -> {
	                    String name = InputUtil.readString("Operator Name: ");
	                    operatorService.addOperator(name, admin.getUserId());
	                }

	                case 2 -> {
	                    System.out.println("Available Operators:");
	                    readService.viewOperators(); 

	                    String name = InputUtil.readString("Enter Operator Name to Activate: ");

	                    operatorService.changeOperatorStatusByName(name, "ACTIVE", admin.getUserId());
	                }

	                case 3 -> {
	                    System.out.println("Available Operators:");
	                    readService.viewOperators();

	                    String name = InputUtil.readString("Enter Operator Name to Deactivate: ");

	                    operatorService.changeOperatorStatusByName(name, "INACTIVE", admin.getUserId());
	                }

	                case 4 -> back = true;
	            }
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    }
	}

    /* ================= PLAN MANAGEMENT ================= */

    private void planManagement() {
        boolean back = false;

        while (!back) {
            System.out.println("""
            		
                === RECHARGE PLAN MANAGEMENT ===
                1. Add Recharge Plan
                2. Update Plan Price
                3. Activate Plan
                4. Deactivate Plan
                5. Back
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 5);

            switch (choice) {
            case 1 -> {
                System.out.println("Available Operators:");
                readService.viewOperators();   

                String operatorName = InputUtil.readString("Select Operator Name: ");

                String planName = InputUtil.readString("Plan Name:");
                double price = InputUtil.readDouble("Price:");
                int validity = InputUtil.readInt("Validity (days):");
                String data = InputUtil.readString("Data Benefits:");
                String call = InputUtil.readString("Call Benefits:");
                String sms = InputUtil.readString("SMS Benefits:");
                String type = InputUtil.readString("Plan Type:");

                RechargePlan plan = new RechargePlan(0, planName, price, validity, data,
                		call, sms, type);

                planService.createPlanByOperatorName(
                        operatorName,
                        plan,
                        admin.getUserId()
                );
            }

                case 2 -> {
                	System.out.println("Available Recharge Plans");
                	readService.viewRechargePlans();
                	
                    String planName = InputUtil.readString("Plan Name:");
                    double price = InputUtil.readDouble("New Price:");
                    planService.updatePlanPrice(planName, price, admin.getUserId());
                }
                case 3 -> {
                	System.out.println("Available Recharge Plans");
                	readService.viewRechargePlans();
                	
                	String planName = InputUtil.readString("Plan Name:");
                    planService.changePlanStatus(planName, true, admin.getUserId());
                }
                case 4 -> {
                	System.out.println("Available Recharge Plans");
                	readService.viewRechargePlans();
                	
                	String planName = InputUtil.readString("Plan Name:");
                    planService.changePlanStatus(planName, false, admin.getUserId());
                }
                case 5 -> back = true;
            }
        }
    }
    
    /* ================= OFFER MANAGEMENT ================= */

    private void offerManagement() {
        boolean back = false;

        while (!back) {
            System.out.println("""
            		
                === OFFER MANAGEMENT ===
                1. Create Offer
                2. Activate Offer
                3. Deactivate Offer
                4. Back
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 4);

            switch (choice) {
                case 1 -> {
                    String title = InputUtil.readString("Offer Title:");
                    String type = InputUtil.readString("Discount Type (FLAT/PERCENTAGE):");
                    double value = InputUtil.readDouble("Discount Value:");

                    try {
                        LocalDate start = LocalDate.parse(InputUtil.readString("Start Date (yyyy-mm-dd):"));
                        LocalDate end = LocalDate.parse(InputUtil.readString("End Date (yyyy-mm-dd):"));

                        Offer offer = new Offer(title, type, value, start, end);
                        offerService.createOfferByTitle(offer, admin.getUserId());
                        
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format.");
                    }
                }
                case 2 -> {
                    System.out.println("Available Offers: ");
                    readService.viewOffers();
                    
                    String title = InputUtil.readString("Enter Offer Titlt to Activate: ");
                    offerService.changeOfferStatusByTitle(title, true, admin.getUserId());
                }
                case 3 -> {
                	System.out.println("Available Offers: ");
                	readService.viewOffers();
                	
                    String title = InputUtil.readString("Enter Offer Title to Deactivate:");
                    offerService.changeOfferStatusByTitle(title, false, admin.getUserId());
                }
                case 4 -> back = true;
            }
        }
    }
    
    /* ================= OFFER–PLAN MAPPING ================= */

    private void offerPlanMapping() {
        boolean back = false;

        while (!back) {
            System.out.println("""
            		
                === OFFER–PLAN MAPPING ===
                1. Map Offer to Plan
                2. Remove Offer from Plan
                3. Back
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 3);

            switch (choice) {
                case 1 -> {
                    System.out.println("Available Operators: ");
                    readService.viewOperators();
                    
                    String operatorName = InputUtil.readString("Operator Name: ");
                    System.out.println("Available Recharge Plans: ");
                    readService.viewRechargePlans();
                    
                    String planName = InputUtil.readString("Plan Name: ");
                    
                    System.out.println("Available Offers: ");
                    readService.viewOffers();
                     
                    String offerTitle = InputUtil.readString("Offer Title: ");
                    
                    int priority;
                    do {
                        priority = InputUtil.readInt("Priority (>=1): ");
                        if (priority < 1) {
                            System.out.println("Invalid priority. It must be 1 or greater.");
                        }
                    } while (priority < 1);

                    
                    offerService.attachOfferToPlanByNames(operatorName, planName, offerTitle, priority, admin.getUserId());
                }
                case 2 -> {
                	System.out.println("Available Operators: ");
                    readService.viewOperators();
                    
                    String operatorName = InputUtil.readString("Operator Name: ");
                    System.out.println("Available Recharge Plans: ");
                    readService.viewRechargePlans();
                    
                    String planName = InputUtil.readString("Plan Name: ");
                    
                    System.out.println("Available Offers: ");
                    readService.viewOffers();
                    
                    String offerTitle = InputUtil.readString("Offer Title: ");
                    
                    offerService.removeOfferFromPlanByNames(operatorName, planName, offerTitle,admin.getUserId());
                }
                case 3 -> back = true;
            }
        }
    }
    
    /* ================= REFUND MANAGEMENT ================= */

    private void refundManagement() {
        boolean back = false;

        while (!back) {
            System.out.println("""
            		
                === REFUND MANAGEMENT ===
                1. Initiate Refund
                2. Complete Refund
                3. Back
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 3);

            switch (choice) {
            case 1 -> {
                String txnRef = InputUtil.readString("Transaction Reference: ");
                String mobile = InputUtil.readString("Mobile Number: ");

                refundService.initiateRefund(txnRef, mobile, admin.getUserId());
            }

            case 2 -> {
                String txnRef = InputUtil.readString("Transaction Reference: ");
                refundService.completeRefund(txnRef, admin.getUserId());
            }

            case 3 -> back = true;
            }
        }
    }
    
}
