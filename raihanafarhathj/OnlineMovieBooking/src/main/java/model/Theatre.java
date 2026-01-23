package model;

import enums.TheatreStatus;
public class Theatre {
	private int _theatreId;
	private String _theatreName;
	private String _contactNumber;
	private TheatreStatus _status;
	private int _addressId;
	
	public Theatre() {
	}
	
	public Theatre(String theatreName, String contactNumber,
			TheatreStatus status,int addressId) {
		this._theatreName = theatreName;
		this._contactNumber = contactNumber;
		this._status = status;
		this._addressId = addressId;
	}
	
	public int getTheatreId() {
		return _theatreId;
	}
	public void setTheatreId(int theatreId) {
		this._theatreId = theatreId;
	}
	
	public int getAddressId() {
		return _addressId;
	}
	public void setAddressId(int addressId) {
		this._addressId = addressId;
	}
	
	public String getTheatreName() {
		return _theatreName;
	}
	
	public void setTheatreName( String theatreName) {
		this._theatreName = theatreName;
	}
	
	public String getContactNumber() {
		return _contactNumber;
	}
	
	public void setContactNumber( String contactNumber) {
		this._contactNumber = contactNumber;
	}
	
	public TheatreStatus getStatus() {
		return _status;
	}
	
	public void setStatus( TheatreStatus status) {
		this._status = status;
	}
	

	
	 public String toString() {
	        return "Theatre [theatreId=" + _theatreId + ", theatreName=" + _theatreName +
	               ", contactNumber=" + _contactNumber + ", status=" + _status +
	               ", addressId=" + _addressId + "]";
	    }
}
