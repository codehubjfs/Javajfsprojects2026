package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;

public class RechargeHistoryDAO {

	// query used to get user recharge history
    private static final String GET_USER_HISTORY =
        """
        select
            mc.mobile_number,
            rp.plan_name,
            rt.final_amount,
            rt.status as recharge_status,
            p.transaction_reference,
            p.status as payment_status,
            rt.initiated_at
        from recharge_transaction rt
        join mobile_connection mc
            on rt.connection_id = mc.connection_id
        join recharge_plan rp
            on rt.plan_id = rp.plan_id
        join payment p
            on rt.recharge_id = p.recharge_id
        where rt.user_id = ?
          and p.attempt_number = (
              select max(attempt_number)
              from payment
              where recharge_id = rt.recharge_id
          )
        order by rt.initiated_at desc
        """;

    /**
     * used to get user history
     * @param userId
     * @return
     */
    public List<String> getUserHistory(int userId) {

        List<String> history = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_USER_HISTORY);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                history.add(
                    rs.getString("mobile_number") + " | " +
                    rs.getString("plan_name") + " | ₹" +
                    rs.getDouble("final_amount") + " | " +
                    rs.getString("recharge_status") + " | " +
                    rs.getString("payment_status") + " | " +
                    rs.getString("transaction_reference")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch recharge history", e);
        }

        return history;
    }
}
