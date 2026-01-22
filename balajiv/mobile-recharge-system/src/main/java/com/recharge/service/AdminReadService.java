package com.recharge.service;

import java.util.List;
import java.util.Map;

import com.recharge.dao.OperatorDAO;
import com.recharge.dao.PaymentDAO;
import com.recharge.dao.RechargeInvoiceDAO;
import com.recharge.dao.RechargePlanDAO;
import com.recharge.dao.RechargeTransactionDAO;

public class AdminReadService {

	private final OperatorDAO operatorDAO = new OperatorDAO();
	private final RechargePlanDAO planDAO = new RechargePlanDAO();
    private final RechargeTransactionDAO txDAO = new RechargeTransactionDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RechargeInvoiceDAO invoiceDAO = new RechargeInvoiceDAO();
    
    public void viewOperators() {
    	System.out.println("\n--- OPERATORS ---");
    	operatorDAO.findAllOperators().forEach(System.out::println);
    }
    
    public void viewRechargePlans() {
    	System.out.println("\n--- RECHARGE PLANS ---");
    	
    	Map<String, List<String>> plans = planDAO.getPlansGroupedByType();
    	plans.forEach((type, list) -> {System.out.println("\n["+type+"]");
    	list.forEach(System.out::println);
    	});
    	
    }
    
    public void viewRechargeTransactions() {
    	System.out.println("\n--- RECHARGE TRANSACTIONS ---");
        txDAO.findAllTransactions().forEach(System.out::println);
    }
    
    public void viewPayments() {
        System.out.println("\n--- PAYMENTS ---");
        paymentDAO.findAllPayments().forEach(System.out::println);
    }

    public void viewInvoices() {
        System.out.println("\n--- INVOICES ---");
        invoiceDAO.findAllInvoices().forEach(System.out::println);
    }
}
