package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;
import com.recharge.model.RechargeInvoice;

public class RechargeInvoiceDAO {
	
	private static final String INSERT_INVOICE = 
			"""
			insert into recharge_invoice(recharge_id, generated_at, invoice_url)
			values(?, now(), ?)
			""";
	
	public void createInvoice(RechargeInvoice invoice) {
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_INVOICE);
			ps.setInt(1, invoice.getRechargeId());
			ps.setString(2,  invoice.getInvoiceUrl());
			
			ps.executeUpdate();
			
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to generate the invoice");
		}
	}
	
	public List<String> findAllInvoices() {

	    List<String> list = new ArrayList<>();

	    String sql =
	        "SELECT invoice_id, recharge_id, invoice_url, generated_at " +
	        "FROM recharge_invoice ORDER BY invoice_id DESC";

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            list.add(
	                rs.getInt("invoice_id") + " | Recharge " +
	                rs.getInt("recharge_id") + " | " +
	                rs.getString("invoice_url") + " | " +
	                rs.getTimestamp("generated_at")
	            );
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to fetch invoices", e);
	    }
	    return list;
	}

}
