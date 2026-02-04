package com.recharge.service;

import java.util.List;

import com.recharge.dao.RechargeHistoryDAO;

public class UserHistoryService {

    private final RechargeHistoryDAO historyDAO = new RechargeHistoryDAO();

    /**
     * used to view Recharge history
     * @param userId
     */
    public void viewHistory(int userId) {

        List<String> history = historyDAO.getUserHistory(userId);

        if (history.isEmpty()) {
            System.out.println("No recharge history found");
            return;
        }

        System.out.println("\n=== RECHARGE HISTORY ===");
        System.out.printf("%-15s %-25s %-10s %-20s %-12s %-15s%n",
                "MOBILE", "PLAN", "AMOUNT", "RECHARGE", "PAYMENT", "TXN REF");
        System.out.println("---------------------------------------------------------------------------------------");

        history.forEach(hist -> {
            String[] h = hist.split("\\|");

            System.out.printf("%-15s %-25s %-10s %-20s %-12s %-15s%n",
                    h[0].trim(),
                    h[1].trim(),
                    h[2].trim(),
                    h[3].trim(),
                    h[4].trim(),
                    h[5].trim()
            );
        });
    }

}
