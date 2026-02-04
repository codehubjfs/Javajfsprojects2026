package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.recharge.config.DBConnection;

public class PlanOfferDAO {

	// query used to insert offer mapping
	private static final String INSERT_MAPPING = 
			"""
			insert into plan_offer
			(plan_id, offer_id, priority)
			values(?,?,?)
			""";
	
	// query used to delete offer mapping
	private static final String DELETE_MAPPING = 
			"""
			delete from plan_offer 
			where plan_id = ? and offer_id = ?
			""";
	
	// query used to get best offer from the plan_offer table based on the priority
	private static final String GET_BEST_OFFER = 
			"""
	        select o.discount_type, o.discount_value
	        from offer o
	        join plan_offer op on o.offer_id = op.offer_id
	        where op.plan_id = ?
	          and o.is_active = true
	          and (o.start_date is null or o.start_date <= curdate())
	          and (o.end_date is null or o.end_date >= curdate())
	        order by op.priority asc
	        limit 1
	        """;
	
	/**
	 * used to attach offer mapping 
	 * @param planId
	 * @param offerId
	 * @param priority
	 */
	
	public void attach(int planId, int offerId, int priority) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_MAPPING);
            ps.setInt(1, planId);
            ps.setInt(2, offerId);
            ps.setInt(3, priority);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to attach offer to plan", e);
        }
    }

	/**
	 * used to remove the offer mapping
	 * @param planId
	 * @param offerId
	 */
	
    public void detach(int planId, int offerId) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_MAPPING);
            ps.setInt(1, planId);
            ps.setInt(2, offerId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to remove offer from plan", e);
        }
    }
    
    /**
     * used to get best offer to plan
     * @param planId
     * @return
     */
    
    public OfferData getBestOfferForPlan(int planId) {
    	try {
    		Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_BEST_OFFER);
            ps.setInt(1, planId);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null; // no offer applicable
            }

            return new OfferData(rs.getString("discount_type"), rs.getDouble("discount_value"));
    	}
    	catch (Exception e) {
    		throw new RuntimeException("Failed to fetch offer for plan", e);
    	}
    }
    
    /* 
     * immutable helper for getting offer data
     */
    public static class OfferData {
        public final String type;
        public final double value;

        public OfferData(String type, double value) {
            this.type = type;
            this.value = value;
        }
    }
}
