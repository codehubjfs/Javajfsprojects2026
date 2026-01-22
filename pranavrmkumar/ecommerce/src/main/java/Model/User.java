package Model;

public class User {
	private int user_id;
	private String name;
	private String email;
	
	public User(int user_id,String name,String email) {
		this.user_id = user_id;
		this.name = name;
		this.email = email;
	}
	
	public int getUserId() {
		return user_id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
}
