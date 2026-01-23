package model;

public class Address {
	private int _addressId;
	private String _streetName;
	private String _areaName;
	private String _landmark;
	private int _pincode;
	private int _cityId;
	
	public Address() {
	}
	public Address(String streetName, String areaName, String landmark, 
			int pincode, int cityId) {
		this._streetName = streetName;
		this._areaName = areaName;
		this._landmark = landmark;
		this._pincode = pincode;
		this._cityId = cityId;
	}
	
	public int getAddressId() {
		return _addressId;
	}
	
	public void setAddressId(int addressId) {
		this._addressId = addressId;
	}
	
	public String getStreetName() {
		return _streetName;
	}
	
	public void setStreetName(String streetName) {
		this._streetName = streetName;
	}
	
	public String getAreaName() {
		return _areaName;
	}
	
	public void setAreaName(String areaName) {
		this._areaName = areaName;
	}
	
	public String getLandmark() {
		return _landmark;
	}
	
	public void setLandmark(String landmark) {
		this._landmark = landmark;
	}
	
	public int getPincode() {
		return _pincode;
	}
	
	public void setPincode(int pincode) {
		this._pincode = pincode;
	}
	
	public int getCityId() {
		return _cityId;
	}
	
	public void setCityId(int cityId) {
		this._cityId = cityId;
	}
	
	 public String toString() {
	        return "Address [addressId=" + _addressId + ", streetName=" + _streetName +
	               ", areaName=" + _areaName + ", landmark=" + _landmark +
	               ", pincode=" + _pincode + ", cityId=" + _cityId + "]";
	    }
}
