package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.recharge.config.DBConnection;
import com.recharge.model.RechargePlan;

public class RechargePlanDAO {

	// used to get all plan from the db
	private static final String GET_ALL = 
			"""
			select plan_id, plan_name, price, validity_days, data_benefits, call_benefits, sms_benefits, plan_type, is_active
			from recharge_plan
			order by plan_id
			""";
	
	// insert a new plan to the db
	private static final String INSERT_PLAN = 
			"""
			insert into recharge_plan
			(operator_id, plan_name, price, validity_days, data_benefits, call_benefits, sms_benefits, plan_type, is_active)
			values(?, ?, ?, ?, ?, ?, ?, ?, true)
			""";
	
	public int createPlan(RechargePlan plan) {
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_PLAN, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, plan.getOperatorId());
            ps.setString(2, plan.getPlanName());
            ps.setDouble(3, plan.getPrice());
            ps.setInt(4, plan.getValidityDays());
            ps.setString(5, plan.getDataBenefits());
            ps.setString(6, plan.getCallBenefits());
            ps.setString(7, plan.getSmsBenefits());
            ps.setString(8, plan.getPlanType());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
            
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to create the recharge plan", e);
		}
	}
	
	public double getPlanPrice(int planId) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select price from recharge_plan where plan_id = ?");
			
			ps.setInt(1, planId);
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {				
				throw new RuntimeException("Plan not found");
			}
			return rs.getDouble("price");
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to get the plan price", e);
		}
	}
	
	public void updatePlanPrice(int plan_id, double newPrice) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("update recharge_plan set price = ? where plan_id = ?");
			
			ps.setDouble(1, newPrice);
			ps.setInt(2, plan_id);
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to update the plan price", e);
		}
	}
	
	public void updatePlanStatus(int plan_id, boolean isActive) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("update recharge_plan set is_active = ? where plan_id = ?");
			
			ps.setBoolean(1, isActive);
			ps.setInt(2, plan_id);
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to update the plan status", e);
		}
	}
	
	public Map<String, List<String>> getPlansGroupedByType(){
		
		List<String[]> rawPlans = new ArrayList<>();
		
		try {
			Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(GET_ALL);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
            	rawPlans.add(new String[]{
            		rs.getString("plan_type"),
            		rs.getInt("plan_id") + " | " + rs.getString("plan_name") + " | ₹" + rs.getDouble("price") + " | " +
            		rs.getInt("validity_days") + " days | " + (rs.getBoolean("is_active") ? "ACTIVE" : "INACTIVE")});
            		
            }  
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch recharge plans", e);
		}
		
		return rawPlans.stream().collect(Collectors.groupingBy(arr -> arr[0].toLowerCase(), TreeMap::new, Collectors.mapping(arr -> arr[1], Collectors.toList())));
	}
}
