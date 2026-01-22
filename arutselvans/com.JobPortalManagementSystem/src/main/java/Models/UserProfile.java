package Models;

import java.time.LocalDate;

public class UserProfile {
	private String name;
	private String email;
	private String phoneNo;
	private LocalDate DataOfBirth;
	private String address;
	private String gender;
	private String designation;
	private int company_id;
	
	public UserProfile(String name,String email,String phoneNo,LocalDate dob , String address,String gender,String designation,int company_id) {
		this.name = name;
		this.email = email;
		this.phoneNo = phoneNo;
		this.DataOfBirth = dob;
		this.address = address;
		this.gender = gender;
		this.designation = designation;
		this.company_id = company_id;
	}
	


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the phoneNo
	 */
	public String getPhoneNo() {
		return phoneNo;
	}
	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	/**
	 * @return the dataOfBirth
	 */
	public LocalDate getDataOfBirth() {
		return DataOfBirth;
	}
	/**
	 * @param dataOfBirth the dataOfBirth to set
	 */
	public void setDataOfBirth(LocalDate dataOfBirth) {
		DataOfBirth = dataOfBirth;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}
	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String toString() {
		return 	"Name   : "+this.name+"\n"+
				"Email  : "+this.email+"\n"+
				"Phone No : "+this.phoneNo+"\n"+
				"Date of Birth : "+this.DataOfBirth+"\n"+
				"Address: "+this.address+"\n"+
				"Gender : "+this.gender+"\n"+
				"Designation :"+this.designation;
		
	}



	/**
	 * @return the company_id
	 */
	public int getCompany_id() {
		return company_id;
	}



	/**
	 * @param company_id the company_id to set
	 */
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
}

