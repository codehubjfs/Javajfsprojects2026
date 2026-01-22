package model;
import java.time.LocalDate;

public class Employee extends Person{
	
	int employeeId;
	String firstName;
	String lastName;
	String skill;
	LocalDate joinDate;
	EmployeeType empType;
	String projectName;
	LocalDate assigned;
	
	public Employee(int employeeId, String firstName, String lastName, String skill,
			LocalDate joinDate, EmployeeType empType,String projectName, LocalDate date,String email, 
			String contact, LocalDate dob, Gender gender, String state, String city,
			String street, String pincode) {
		super(email, contact, dob, gender, state, city, street, pincode);
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.skill = skill;
		this.joinDate = joinDate;
		this.empType = empType;
		this.projectName = projectName;
		this.assigned = date;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public EmployeeType getEmpType() {
		return empType;
	}

	public void setEmpType(EmployeeType empType) {
		this.empType = empType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public LocalDate getAssigned() {
		return assigned;
	}

	public void setAssigned(LocalDate assigned) {
		this.assigned = assigned;
	}

	@Override
	public String toString() {
		return String.format(
		        "| %-10d | %-10s | %-10s | %-10s | %-10s | %-10s | %-18s | %-10s | %-20s "
		        + "| %-12s | %-10s | %-8s | %-10s | %-10s | %-10s | %-8s |",employeeId,
		        firstName,lastName,skill,joinDate,empType,projectName,assigned,email,
		        contact,dob,gender,state,city,street,pincode
		);

	}

}
