package Models;

import java.time.LocalDate;

public class User {
	private int user_id;
	private String name;
	private String role;
	private String email;
	private String gender;
	private LocalDate dateOfBirth;
	private String phoneNo;
	
	
	public User(int user_id,String name , String role,String email,String gender,LocalDate dateOfBirth , String phoneNo){
		this.user_id = user_id;
		this.name = name;
		this.role = role;
		this.phoneNo = phoneNo;
		this.email = email;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
	}
	
	 public int getId() { return user_id; }
	    public String getName() { return name; }
	    public String getRole() { return role; }
	    public String getPhoneNo() { return phoneNo; }
	    
	  public String toString() {
		  return "Name: "+this.name+"| Role: "+this.role+" | Email: "+this.email+" | Gender: "+this.gender+" | Date of Birth:"+this.dateOfBirth+"| User_id: "+this.user_id+"| Phone No: "+this.phoneNo;
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
 	  * @return the genger
 	  */
	  public String getGender() {
		return gender;
	  }

	  /**
 	  * @param genger the genger to set
 	  */
	  public void setGenger(String gender) {
		this.gender = gender;
	  }

	  /**
 	  * @return the dateOfBirth
 	  */
	  public LocalDate getDateOfBirth() {
		return dateOfBirth;
	  }

	  /**
 	  * @param dateOfBirth the dateOfBirth to set
 	  */
	  public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	  }
}
