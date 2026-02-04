package com.recharge.service;

import java.util.List;
import java.util.Map;

import com.recharge.dao.OperatorDAO;
import com.recharge.dao.PaymentDAO;
import com.recharge.dao.RechargeInvoiceDAO;
import com.recharge.dao.RechargePlanDAO;
import com.recharge.dao.RechargeTransactionDAO;
import com.recharge.dao.OfferDAO;

public class AdminReadService {

	private final OperatorDAO operatorDAO = new OperatorDAO();
	private final RechargePlanDAO planDAO = new RechargePlanDAO();
    private final RechargeTransactionDAO txDAO = new RechargeTransactionDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final RechargeInvoiceDAO invoiceDAO = new RechargeInvoiceDAO();
    private final OfferDAO offerDAO = new OfferDAO();
    
    // used to view all available operators
    public void viewOperators() {
        System.out.println("\n--- OPERATORS ---\n");
        System.out.printf("%-15s %-10s%n", "OPERATOR", "STATUS");
        System.out.println("-----------------------");

        operatorDAO.findAllOperators().forEach(op -> {
            String[] parts = op.split("\\|");
            System.out.printf("%-15s %-10s%n",
                    parts[0].trim(),
                    parts[1].trim()
            );
        });
    }

    // used to view all available recharge plans
    public void viewRechargePlans() {
        System.out.println("\n--- RECHARGE PLANS ---");

        Map<String, List<String>> plans = planDAO.getPlansGroupedByType();

        plans.forEach((type, list) -> {
            System.out.println("\n[" + type.toUpperCase() + "]");
            System.out.printf("%-4s %-25s %-10s %-10s %-10s%n",
                    "ID", "PLAN NAME", "PRICE", "VALIDITY", "STATUS");
            System.out.println("---------------------------------------------------------------");

            list.forEach(ls -> {
                String[] p = ls.split("\\|");
                System.out.printf("%-4s %-25s %-10s %-10s %-10s%n",
                        p[0].trim(),
                        p[1].trim(),
                        p[2].trim(),
                        p[3].trim(),
                        p[4].trim()
                );
            });
        });
    }

    
    // used to view all recharge transactions
    public void viewRechargeTransactions() {
        System.out.println("\n--- RECHARGE TRANSACTIONS ---");

        System.out.printf("%-4s %-10s %-10s %-20s %-22s %-22s%n",
                "ID", "USER", "AMOUNT", "STATUS", "CREATED AT", "UPDATED AT");
        System.out.println("---------------------------------------------------------------------------------------------");

        txDAO.findAllTransactions().forEach(tx -> {
            String[] t = tx.split("\\|");
            System.out.printf("%-4s %-10s %-10s %-20s %-22s %-22s%n",
                    t[0].trim(),
                    t[1].trim(),
                    t[2].trim(),
                    t[3].trim(),
                    t[4].trim(),
                    t[5].trim()
            );
        });
    }

    
    // used to view all payments done 
    public void viewPayments() {
        System.out.println("\n--- PAYMENTS ---");

        System.out.printf("%-4s %-12s %-10s %-10s %-10s%n",
                "ID", "RECHARGE", "ATTEMPT", "AMOUNT", "STATUS");
        System.out.println("-----------------------------------------------------------");

        paymentDAO.findAllPayments().forEach(p -> {
            String[] pay = p.split("\\|");
            System.out.printf("%-4s %-12s %-10s %-10s %-10s%n",
                    pay[0].trim(),
                    pay[1].trim(),
                    pay[2].trim(),
                    pay[3].trim(),
                    pay[4].trim()
            );
        });
    }


    // used to view invoices after succesfull recharge.
    public void viewInvoices() {
        System.out.println("\n--- INVOICES ---");

        System.out.printf("%-4s %-12s %-25s %-22s%n",
                "ID", "RECHARGE", "FILE", "CREATED AT");
        System.out.println("--------------------------------------------------------------------");

        invoiceDAO.findAllInvoices().forEach(inv -> {
            String[] i = inv.split("\\|");
            System.out.printf("%-4s %-12s %-25s %-22s%n",
                    i[0].trim(),
                    i[1].trim(),
                    i[2].trim(),
                    i[3].trim()
            );
        });
    }

    
    // used to view all available offers
    public void viewOffers() {
        System.out.println("\n---- OFFERS ----");

        System.out.printf("%-30s %-10s %-15s%n",
                "OFFER NAME", "STATUS", "VALIDITY");
        System.out.println("-------------------------------------------------------------");

        offerDAO.findAllOffers().forEach(of -> {
            String[] o = of.split("\\|");

            System.out.printf("%-30s %-10s %-15s%n",
                    o[0].trim(),
                    o[1].trim(),
                    o[2].trim()
            );
        });
    }

    
    // used to view available plans for particular operator
    public void viewActivePlansByOperator(int operatorId) {
        Map<String, List<String>> plans = planDAO.getActivePlansByOperator(operatorId);

        if (plans.isEmpty()) {
            System.out.println("No active plans available for this operator");
            return;
        }

        System.out.println("\n--- ACTIVE RECHARGE PLANS ---");

        plans.forEach((type, list) -> {
            System.out.println("\n[" + type.toUpperCase() + "]");

            System.out.printf("%-4s %-30s %-10s %-10s%n",
                    "ID", "PLAN NAME", "PRICE", "VALIDITY");
            System.out.println("----------------------------------------------------------");

            list.forEach(ls -> {
                String[] p = ls.split("\\|");

                System.out.printf("%-4s %-30s %-10s %-10s%n",
                        p[0].trim(),
                        p[1].trim(),
                        p[2].trim(),
                        p[3].trim()
                );
            });
        });
    }

}
