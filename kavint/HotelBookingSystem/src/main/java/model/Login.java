package model;

public class Login {

    private int loginId;
    private int userId;
    private String password;
    private String lastLogin;
    private String createdAt;

    public Login(int loginId, int userId, String password,
                 String lastLogin, String createdAt) {
        this.loginId = loginId;
        this.userId = userId;
        this.password = password;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
    }

	public int getLoginId() {
		return loginId;
	}

	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

    
    
}

