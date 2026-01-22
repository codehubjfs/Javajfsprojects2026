package com.modals;

import java.time.*;

public class Route{
	private int _routeID;
	private String _source;
	private String _destination;
	private int _distance;
	private LocalTime _estimatedTime;
	
	public Route(int routeID,String source,String destination,int distance,LocalTime estimatedTime) {
		this._routeID = routeID;
		this._source = source;
		this._destination = destination;
		this._distance = distance;
		this._estimatedTime = estimatedTime;
	}
	
	public String getSource() {return _source;}
	public String getDestination() {return _destination;}
	public int getDistance() {return _distance;}
	public LocalTime getEstimatedTime() {return _estimatedTime;}
	
	public String toString() {
		return "Source : " + getSource() + " | Destination : " + getDestination() + " | Distance : "+getDistance()+
				" | Estimated Time : " + getEstimatedTime();
	}
	
}
