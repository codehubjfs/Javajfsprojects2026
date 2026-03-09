package com.ecommerce.models;

public class Address {
	private int address_id;
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String address_type;
	
	public Address(int address_id,String street,String city,String state,String zipcode,String address_type) {
		this.address_id = address_id;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.address_type = address_type;
	}
	public int getAddress_id() {
		return address_id;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	public String getZipcode() {
		return zipcode;
	}
	
	public String getAddress_type() {
		return address_type;
	}
	
}
