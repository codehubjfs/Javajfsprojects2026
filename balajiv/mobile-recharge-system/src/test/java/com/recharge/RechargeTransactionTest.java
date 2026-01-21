package com.recharge;

import com.recharge.config.DBConnection;
import com.recharge.dao.RechargeTransactionDAO;
import com.recharge.model.RechargeTransaction;
import com.recharge.service.PostRechargeService;

public class RechargeTransactionTest {

    public static void main(String[] args) {

        DBConnection.initialize(); 
        
        //for testing rechargetransaction flow.

        RechargeTransaction tx = new RechargeTransaction(8, 7, 6, 299.0);

        RechargeTransactionDAO dao = new RechargeTransactionDAO();

        int id = dao.createInitiatedTransaction(tx);

        dao.updateStatus(id, "PAYMENT_IN_PROGRESS");
        dao.updateStatus(id, "SUCCESS");
        
        //for testing invoice and notification generations.
        
        new PostRechargeService().handleSuccessfulRecharge(7, 8);
        DBConnection.close();
    }
}
