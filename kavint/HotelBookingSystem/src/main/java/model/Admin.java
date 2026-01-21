package model;

public class Admin extends Employee {

	private int adminId;
	
    public Admin(int userId, String name, String email, String phone, String gender, Location location, String idProof,
			String createdAt, String lastLogin, String status, int employeeId, int roleId, int departmentId,
			String shift, int adminId) {
		super(userId, name, email, phone, gender, location, idProof, createdAt, lastLogin, status, employeeId, roleId,
				departmentId, shift);
		this.adminId = adminId;
	}

    public int getAdminId() { 
    	return adminId; 
    }
    
    public void setAdminId(int adminId) { 
    	this.adminId = adminId; 
    }
}

