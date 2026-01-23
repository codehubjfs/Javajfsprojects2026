package model;
import enums.SeatType;
public class Seat {
	private int _seatId;
	private String _seatNumber;
	private  SeatType _seatType;
	private int _hallId;

	public Seat() {	}
	
	public Seat(String seatNumber, SeatType seatType, int hallId) {
		this._seatNumber = seatNumber;
		this._seatType = seatType;
		this._hallId = hallId;
	}
	
	public int getSeatId() {
		return _seatId;
	}
	
	public String getSeatNumber() {
		return _seatNumber;
	}
	
	public void setSeatNumber(String seatNumber) {
		this._seatNumber = seatNumber;
	}
	
	public SeatType getSeatType() {
		return _seatType;
	}
	
	public void setSeatType(SeatType seatType) {
		this._seatType = seatType;
	}
	
	 public int getHallId() {
	        return _hallId;
	 }

	 public void setHallId(int hallId) {
		 this._hallId = hallId;
	}
	 
	 public String toString() {
	        return "Seat{" +
	                "_seatId=" + _seatId +
	                ", _seatNumber='" + _seatNumber + '\'' +
	                ", _seatType=" + _seatType +
	                ", _hallId=" + _hallId +
	                '}';
	    }
}
