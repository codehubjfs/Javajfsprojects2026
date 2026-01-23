package model;

public class City {
	private int _cityId;
	private String _cityName;
	private String _state;
	
	public City() {	
	}
	
	public City(String cityName, String state) {
		this._cityName = cityName;
		this._state = state;
	}
	
	public int getCityId() {
		return _cityId;
	}
	
	public void setCityId(int cityId) {
		this._cityId = cityId;
	}
	
	public String getCityName() {
		return _cityName;
	}
	
	public void setCityName(String cityName) {
		this._cityName = cityName;
	}
	
	public String getState() {
		return _state;
	}
	
	public void setState(String state) {
		this._state = state;
	}
	
	public String toString() {
		return "City [cityId=" + _cityId + "CityName="+ _cityName + ", State=" + _state + "]";
	}
	
	
}
