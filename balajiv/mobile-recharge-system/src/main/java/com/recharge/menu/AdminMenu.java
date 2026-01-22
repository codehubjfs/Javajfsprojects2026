package com.recharge.menu;


import com.recharge.model.RechargePlan;
import com.recharge.model.User;
import com.recharge.service.AdminOperatorService;
import com.recharge.service.AdminReadService;
import com.recharge.service.AdminRechargePlanService;
import com.recharge.util.InputUtil;


public class AdminMenu {
	
	private final User admin;
	private final AdminReadService readService = new AdminReadService();
	private final AdminOperatorService operatorService = new AdminOperatorService();
	private final AdminRechargePlanService planService = new AdminRechargePlanService();
	
	
	public AdminMenu(User admin) {
		this.admin = admin;
	}
	
	public void start() {
		boolean running = true;
		
		while(running) {
			System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("Welcome, " + admin.getFullName());
            String string = """
            1. View Operators
            2. View Recharge Plans
            3. View Recharge Transactions
            4. View Payments
            5. View Invoices
            6. Add Operator
            7. Activate Operator
            8. Deactivate Operator
            9. Add Recharge Plan
           10. Update Recharge Plan Price
           11. Activate Plan
           12. Deactive Plan
           13. Logout
            ================
            """;
            System.out.println(string);
            
            int choice = InputUtil.readInt("Choose option:", 1, 13);
            try {
	            switch (choice) {
		            	case 1 -> readService.viewOperators();
		            	
		            	case 2 -> readService.viewRechargePlans();
	            	
		            case 3 -> readService.viewRechargeTransactions();
		            
		            case 4 -> readService.viewPayments();
		            
		            case 5 -> readService.viewInvoices();
		            
		            case 6 -> {
			            	String name = InputUtil.readString("Operator Name: ");
			            	operatorService.addOperator(name, admin.getUserId());
		            }
		            
		            case 7 ->{
			            	int id = InputUtil.readInt("Operator ID: ");
			            	operatorService.changeOperatorStatus(id, "ACTIVE", admin.getUserId());
		            }
		            
		            case 8 -> {
			            	int id = InputUtil.readInt("Operator ID: ");
			            	operatorService.changeOperatorStatus(id, "INACTIVE", admin.getUserId());
		            }
		            
		            case 9 -> {
		            		int operatorId = InputUtil.readInt("Operator ID:");
		                String name = InputUtil.readString("Plan Name:");
		                double price = InputUtil.readDouble("Price: ");
		                int validity = InputUtil.readInt("Validity (days):");
		                String dataBenefits = InputUtil.readString("Data Benefits: ");
		                String callBenefits = InputUtil.readString("Call Benefits: ");
		                String smsBenefits = InputUtil.readString("SMS Benefits: ");
		                String type = InputUtil.readString("Plan Type:");

		                RechargePlan plan = new RechargePlan(operatorId, name, price, validity, dataBenefits, callBenefits,
		                		smsBenefits, type);

		                planService.createPlan(plan, admin.getUserId());
		            }
		            
		            case 10 -> {
		            		int planId = InputUtil.readInt("Plan ID: ");
		            		double planPrice = InputUtil.readDouble("Recharge Plan Price: ");
		            		planService.updatePlanPrice(planId, planPrice, admin.getUserId());
		            }
		            
		            case 11 -> {
		            		int id = InputUtil.readInt("Plan ID: ");
		            		planService.changePlanStatus(id, true, admin.getUserId());
		            }
		            
		            case 12 -> {
		            		int id = InputUtil.readInt("Plan ID: ");
		            		planService.changePlanStatus(id, false, admin.getUserId());
		            }
		            
		            case 13 -> running = false;
	            }
            }
            catch(Exception e) {
            	System.out.println(e.getMessage());
            }
		}
	}
}
