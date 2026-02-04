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
	
	// query used to get planId
	private static final String GET_PLAN_ID = 
			"""
            select rp.plan_id
            from recharge_plan rp
            join operator o on rp.operator_id = o.operator_id
            where o.operator_name = ? and rp.plan_name = ? 
			""";
	
	// query used to get active planId
	private static final String GET_ACTIVE_PLAN_ID = 
			"""
			select plan_id
            from recharge_plan
            where operator_id = ?
              and lower(plan_name) = lower(?)
              and is_active = true
			""";
	
	private static final String GET_ACTIVE_PLANBY_OPERATORID = 
			"""
			select plan_type, plan_id, plan_name, price, validity_days
			from recharge_plan
			where operator_id = ?
				and is_active = true
			order by plan_type, price
			""";
	
	// used to create recharge plan in the db
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
	
	// used to get the plan price from the db
	public double getPlanPrice(String planName) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select price from recharge_plan where lower(plan_name) = lower(?)");
			
			ps.setString(1, planName);
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
	
	public double getPlanPriceById(int planId) {
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
	
	// used to update the plan price in the db
	public void updatePlanPrice(String plan_name, double newPrice) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("update recharge_plan set price = ? where lower(plan_name) = lower(?)");
			
			ps.setDouble(1, newPrice);
			ps.setString(2, plan_name);
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to update the plan price", e);
		}
	}
	
	// used to update the plan status to the db
	public void updatePlanStatus(String plan_name, boolean isActive) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("update recharge_plan set is_active = ? where lower(plan_name) = lower(?)");
			
			ps.setBoolean(1, isActive);
			ps.setString(2, plan_name);
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to update the plan status", e);
		}
	}
	
	// used to get planID using operator and its name
	public int getPlanIdByOperatorAndName(String operatorId, String planName) {
	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(GET_PLAN_ID);

	        ps.setString(1, operatorId);
	        ps.setString(2, planName);

	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {
	            throw new RuntimeException("Recharge plan not found");
	        }
	        return rs.getInt("plan_id");
	    }
	    catch (Exception e) {
	        throw new RuntimeException("Failed to resolve recharge plan", e);
	    }
	}
	
	// used to get acitve planId using operatorId and planName
	public int getActivePlanId(int operatorId, String planName) {
		try {			
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ACTIVE_PLAN_ID);
			
			ps.setInt(1, operatorId);
			ps.setString(2, planName);
			
			ResultSet rs = ps.executeQuery();
			    if (!rs.next()) {
			        throw new RuntimeException("Plan not found or inactive");
			}
			
			return rs.getInt("plan_id");
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to resolve recharge plan", e);
		}
	}
	
	// used to get planId using planName
	public int getPlanIdByName(String planName) {
		try {			
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement("select plan_id from recharge_plan where lower(plan_name) = lower(?)");
			
			ps.setString(1, planName);
			
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {
	            throw new RuntimeException("Plan not found: " + planName);
	        }
	
			return rs.getInt("plan_id");
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch plan id by name", e);
		}
	}
	

	public Map<String, List<String>> getActivePlansByOperator(int operatorId){
		List<String[]> rawPlans = new ArrayList<>();
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ACTIVE_PLANBY_OPERATORID);
			
			ps.setInt(1, operatorId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				rawPlans.add(new String[]{
					rs.getString("plan_type"),
					rs.getInt("plan_id") + " | " +
	                rs.getString("plan_name") + " | ₹" +
	                rs.getDouble("price") + " | " +
	                rs.getInt("validity_days") + " days"
				});
			}
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch active plans for operator.");
		}
		
		return rawPlans.stream().collect(Collectors.groupingBy(arr -> arr[0].toLowerCase(),
				() -> new TreeMap<String, List<String>>(), Collectors.mapping(arr -> arr[1], Collectors.toList())));
	}
	
	// it return the map that contains plan which is grouped by plan type 
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
		  
		return rawPlans.stream().collect(Collectors.groupingBy(arr -> arr[0].toLowerCase(),
			            () -> new TreeMap<String, List<String>>(), Collectors.mapping(arr -> arr[1], Collectors.toList())));

	}
}
