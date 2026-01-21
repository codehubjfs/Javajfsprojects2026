package model;

public class Employee extends User {

    protected int employeeId;
    protected int roleId;
    protected int departmentId;
    protected String shift;

    
    
    public Employee(int userId, String name, String email, String phone, String gender, Location location, String idProof,
			String createdAt, String lastLogin, String status, int employeeId, int roleId, int departmentId, String shift) {
		super(userId, name, email, phone, gender, location, idProof, createdAt, lastLogin, status);
		this.employeeId = employeeId;
		this.roleId = roleId;
		this.departmentId = departmentId;
		this.shift = shift;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}
   
}

