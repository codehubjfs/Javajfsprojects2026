package model;

public class Guest extends User {

    private int guestId;

    
    public Guest(int userId, String name, String email, String phone, String gender, Location location, String idProof,
			String createdAt, String lastLogin, String status, int guestId) {
		super(userId, name, email, phone, gender, location, idProof, createdAt, lastLogin, status);
		this.guestId = guestId;
	}

    public int getGuestId() { 
    	return guestId; 
    }
    
    public void setGuestId(int guestId) { 
    	this.guestId = guestId; 
    }
}


