package com.modals;

public class Buses{
	private int _busID;
	private String _busNo;
	private int _totalSeat;
	private String _busType;
	private String _busStatus;	
	private int _operatorID;
	
	public Buses(int busID,String busNo,int totalSeat,String busType,String busStatus,int operatorID){
		this._busID = busID;
		this._busNo = busNo;
		this._totalSeat = totalSeat;
		this._busType = busType;
		this._busStatus = busStatus;
		this._operatorID = operatorID;
	}
	
	public String getbusNo() {return _busNo;}
	public String getBusType() {return _busType;}
	public String getBusStatus() {return _busStatus;}
	public int getOperatorID() {return _operatorID;}
	
	
	public String toString() {
		return "Bus Number : " + getbusNo() + " | Bus Type : " + getBusType() + " | Bus Status : " + getBusStatus();
	}
	
	
}