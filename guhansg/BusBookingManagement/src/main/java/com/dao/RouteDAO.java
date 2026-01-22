package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.*;
import com.util.*;
import com.modals.*;


public class RouteDAO {
	
	public void addNewRoute(String source,String destination,int distance,LocalTime estimateTime)throws Exception{
		if (estimateTime == null) {
		    throw new IllegalArgumentException("Estimated time cannot be null");
		}
		String sql = "insert into Route (source,destination,distance,estimated_time) values(?,?,?,?)";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, source);
			ps.setString(2, destination);
			ps.setInt(3, distance);
			ps.setTime(4,java.sql.Time.valueOf(estimateTime));
			ps.executeUpdate();
		}
	}

	public void removeExistingRoute(String source,String destination) throws Exception{
		String sql = "delete from Route where source = ? and destination = ?";
		try(Connection con = BusDBUtil.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
		   ps.setString(1, source);
		   ps.setString(1, destination);
		   ps.executeUpdate();
		}
	}
	
	public void viewAllAvailableRoutes() throws Exception {
		String sql = "select * from Route";
		try(Connection con = BusDBUtil.getConnection();Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("Source : "+rs.getString("source") + 
						" | Destination : " + rs.getString("destination") +
						" | Distance : "+rs.getInt("distance")+
						" | Estimated time : "+rs.getTime("estimated_time"));
			}
		}
				
	}
	
	public static List<Route> getRoute() throws Exception{
		List<Route> routeList = new ArrayList<>();
		String sql = "select * from Route";
		try(Connection con = BusDBUtil.getConnection();Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)){
			while(rs.next()) {
				Route route = new Route(
						rs.getInt("route_id"),
						rs.getString("source"),
						rs.getString("destination"),
						rs.getInt("distance"),
						rs.getTime("estimated_time").toLocalTime()
						);
				routeList.add(route);
			}
	    }
		return routeList;
	}
}

