package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.util.*;
import com.modals.*;
import java.util.*;

public class BusDAO {
	
	public static void addNewBus(String busNo,int totalSeat,String busType,String busStatus,int operatorID)throws Exception{
		if (busNo == null || busNo.trim().isEmpty()) {
	        throw new Exception("Bus number cannot be empty");
	    }
		String sql = "insert into Bus (bus_no,total_seat,bus_type,bus_status,operator_id) values(?,?,?,?,?)";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, busNo);
			ps.setInt(2, totalSeat);
			ps.setString(3, busType);
			ps.setString(4, busStatus);
			ps.setInt(5, operatorID);
			ps.executeUpdate();
		}
	}

	public static void removeExistingBus(String busNo,int operatorID) throws Exception{
		String sql = "delete from Bus where bus_no = ? and operator_id = ?";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
		   ps.setString(1, busNo);
		   ps.setInt(3, operatorID);
		   ps.executeUpdate();
		}
	}
	
	public static void viewAllBuses() throws Exception {
		String sql = "select bus_no,bus_type,bus_status from Bus ";
		try(Connection con = BusDBUtil.getConnection();Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("Bus Number : "+rs.getString("bus_no")+
						" | Bus Type : "+rs.getString("bus_type") + " | Bus Status : "+rs.getString("bus_status"));
			}
		}
				
	}
	
	public static void updateBusDetail(String busNo,String busType,String busStatus,int operatorID)throws Exception{
		String sql = "update Bus set bus_type = ?,bus_status = ? where bus_no = ? and operator_id = ?";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, busType);
			ps.setString(2, busStatus);
			ps.setString(3, busNo);
			ps.setInt(4, operatorID);
			ps.executeUpdate();
		}
	}
	
	public static void updateBusAvailability(String busNo,String busStatus,int operatorID)throws Exception{
		String sql = "update Bus set bus_status = ? where bus_no = ? and operator_id = ?";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, busStatus);
			ps.setString(2, busNo);
			ps.setInt(3, operatorID);
			ps.executeUpdate();
		}
	}
	
	public List<Buses> getBuses() throws Exception{
		List<Buses> busList = new ArrayList<>();
		
		String sql = "select * from Bus";
		try(Connection con = BusDBUtil.getConnection();Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				Buses bus = new Buses(
						rs.getInt("bus_id"),
						rs.getString("bus_no"),
						rs.getInt("total_seat"),
						rs.getString("bus_type"),
						rs.getString("bus_status"),
						rs.getInt("operator_id")
						);
				busList.add(bus);
			}
			
		}
		return busList;
	}
	

}

