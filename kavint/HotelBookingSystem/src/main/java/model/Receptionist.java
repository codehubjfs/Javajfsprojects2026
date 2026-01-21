package model;

public class Receptionist extends Employee {

	private int receptionistId;
	
    public Receptionist(int userId, String name, String email, String phone, String gender, Location location,
			String idProof, String createdAt, String lastLogin, String status, int employeeId, int roleId,
			int departmentId, String shift) {
		super(userId, name, email, phone, gender, location, idProof, createdAt, lastLogin, status, employeeId, roleId,
				departmentId, shift);
		this.receptionistId = employeeId;
		
	}  

    public int getReceptionistId() { 
    	return receptionistId; 
    }
    
    public void setReceptionistId(int receptionistId) {
        this.receptionistId = receptionistId;
    }
}

