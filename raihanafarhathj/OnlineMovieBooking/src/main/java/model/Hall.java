package model;

public class Hall {
	private int _hallId;
	private  String _hallName;
	private int _totalSeats;
	private int _theatreId;
	
	public Hall() {
	}
	
	public Hall(String hallName, int totalSeats,int theatreId) {
		this._hallName = hallName;
		this._totalSeats = totalSeats;
		this._theatreId = theatreId; 
	}
	
	public int getHallId() {
		return _hallId;
	}
	
	public String getHallName() {
		return _hallName;
	}
	
	public void setHallName(String hallName) {
		this._hallName = hallName;
	}
	
	public int getTotalSeats() {
		return _totalSeats;
	}
	
	public void setTotalSeats(int totalSeats) {
		this._totalSeats = totalSeats;
	}
	
	public int getTheatreId() {
        return _theatreId;
    }

    public void setTheatreId(int theatreId) {
        this._theatreId = theatreId;
    }
	
    public String toString() {
        return "Hall{" +
                "_hallId=" + _hallId +
                ", _hallName='" + _hallName + '\'' +
                ", _totalSeats=" + _totalSeats +
                ", _theatreId=" + _theatreId +
                '}';
    }
}
