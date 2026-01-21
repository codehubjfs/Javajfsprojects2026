package model;

public class User {

    protected int userId;
    protected String name;
    protected String email;
    protected String phone;
    protected String gender;
	protected Location location;
    protected String idProof;
    protected String createdAt;
    protected String lastLogin;
    protected String status;

    public User(int userId, String name, String email, String phone,
                String gender, Location location, String idProof, 
                String createdAt, String lastLogin, String status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.location = location;
        this.idProof = idProof;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.status = status;
    }

    public int getUserId() { 
    	return userId; 
    }

	public void setUserId(int userId) { 
    	this.userId = userId; 
    }

    public String getName() { 
    	return name; 
    }
    
    public void setName(String name) { 
    	this.name = name; 
    }

    public String getEmail() { 
    	return email; 
    }
    
    public void setEmail(String email) { 
    	this.email = email; 
    }

    public String getPhone() { 
    	return phone; 
    }
    
    public void setPhone(String phone) { 
    	this.phone = phone; 
    }

    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
    public String getIdProof() { 
    	return idProof; 
    }
    
    public void setIdProof(String idProof) { 
    	this.idProof = idProof; 
    }

    public String getCreatedAt() { 
    	return createdAt; 
    }
    
    public void setCreatedAt(String createdAt) { 
    	this.createdAt = createdAt; 
    }

    public String getLastLogin() { 
    	return lastLogin; 
    }
    
    public void setLastLogin(String lastLogin) { 
    	this.lastLogin = lastLogin; 
    }

    public String getStatus() { 
    	return status; 
    }
    
    public void setStatus(String status) { 
    	this.status = status; 
    }
}
