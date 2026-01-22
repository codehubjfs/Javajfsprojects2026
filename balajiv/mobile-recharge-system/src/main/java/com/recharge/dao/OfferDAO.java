package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.recharge.config.DBConnection;
import com.recharge.model.Offer;

public class OfferDAO {
	
	private static final String INSERT_OFFER = 
			"""
			insert into offer
			(title, discount_type, discount_value, start_date, end_date)
			values(?, ?, ?, ?, ?, true)
			""";
	
	private static final String UPDATE_STATUS = "update offer set is_active = ? where offer_id = ?";
	
	public int createOffer(Offer offer) {
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_OFFER, PreparedStatement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, offer.getTitle());
			ps.setString(2, offer.getDiscountType());
			ps.setDouble(3, offer.getDiscountValue());
			ps.setDate(4, java.sql.Date.valueOf(offer.getStartDate()));
			ps.setDate(5, java.sql.Date.valueOf(offer.getEndDate()));			
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
				
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to create the offers", e);
		}
	}
	
	public void updateOfferStatus(int offerId, boolean active) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS);
            ps.setBoolean(1, active);
            ps.setInt(2, offerId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update offer status", e);
        }
    }
}
