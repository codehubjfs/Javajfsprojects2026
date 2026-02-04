package com.recharge.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;
import com.recharge.model.Offer;

public class OfferDAO {
	
	// query used to insert offer to the db
	private static final String INSERT_OFFER = 
			"""
			insert into offer
			(title, discount_type, discount_value, start_date, end_date, is_active)
			values(?, ?, ?, ?, ?, true)
			""";
	
	// query used to update status of the offer
	private static final String UPDATE_STATUS = "update offer set is_active = ? where offer_id = ?";
	
	// query used to get the offer count
	private static final String OFFER_COUNT = "select count(*) from offer where title = ?";
	
	// query used to get ghe offer id
	private static final String OFFER_ID = "select offer_id from offer where title = ?";
	
	// query used to implement the auto expire of offers
	private static final String AUTO_EXPIRE_OFFERS = 
			"""
			UPDATE offer SET is_active = 0
		    WHERE is_active = 1
		    AND end_date IS NOT NULL
		    AND end_date < CURRENT_DATE
			""";
	
	/**
	 * used to create the offer 
	 * @param offer
	 * @return offerId
	 */
	
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
			throw new RuntimeException("Failed to create the offers");
		}
	}
	
	/**
	 * used to update the offer status
	 * @param offerId
	 * @param active
	 */
	
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
	
	/**
	 * used to update the offer status using offer title
	 * @param title
	 * @param active
	 */
	
	public void updateOfferStatusByTitle(String title, boolean active) {
	    int offerId = getOfferIdByTitle(title);
	    updateOfferStatus(offerId, active);
	}

	/**
	 * used to check offer exists in the db using offer title
	 * @param title
	 * @return
	 */
	
	public boolean offerExistsByTitle(String title) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(OFFER_COUNT);
			ps.setString(1, title);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1) > 0;
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to check offer existence", e);
		}
	}
	
	/**
	 * used to get offerId by the offer title
	 * @param title
	 * @return
	 */
	
	public int getOfferIdByTitle(String title) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(OFFER_ID);
			ps.setString(1, title);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				throw new RuntimeException("Offer not fount");
			}
			return rs.getInt("offer_id");
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to resolve offer", e);
		}
	}
	
	/**
	 * used to check offer is active or not
	 * @param offerId
	 * @return
	 */
	
	public boolean isOfferActive(int offerId) {
		try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(
	            "select is_active from offer where offer_id = ?"
	        );
	        ps.setInt(1, offerId);

	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        return rs.getBoolean("is_active");

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to check offer status", e);
	    }
	}
	
	/**
	 * used to implement auto expire offer 
	 */
	
	public void autoExpireOffers() {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(AUTO_EXPIRE_OFFERS);
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to auto-expire offers", e);
		}
	}
	
	/**
	 * used to get all available offers
	 * @return
	 */
	
	public List<String> findAllOffers(){
		List<String> offers = new ArrayList<>();
		
		try {
			// auto expire old offers before fetching
			autoExpireOffers();
			
			Connection conn = DBConnection.getConnection();
			String getOffers = "select title, is_active, end_date from offer order by title";
			PreparedStatement ps = conn.prepareStatement(getOffers);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				String title = rs.getString("title");
				boolean isActive = rs.getBoolean("is_active");
				
				String status = isActive ? "ACTIVE" : "INACTIVE";

	            Date endDate = rs.getDate("end_date");
	            String remaining;
	            
	            if (endDate == null) {
	                remaining = "No expiry";
	            } else {
	                long diffMillis = endDate.getTime() - System.currentTimeMillis();
	                long days = diffMillis / (1000 * 60 * 60 * 24);
	                remaining = days >= 0 ? days + " days left" : "Expired";
	            }
	            
	            offers.add(title + " | " + status + " | " + remaining);
			}
			
			return offers;
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch offers", e);
		}
	}
}
