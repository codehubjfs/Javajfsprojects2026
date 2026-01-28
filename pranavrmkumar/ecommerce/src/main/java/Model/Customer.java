package Model;

public class Customer extends User{
	private String status;
	private String password;
	public Customer(String name,String email,String status) {
		super(name,email,"customer");
		this.status = status;
	}
	
	public Customer(String name, String email, String password, String status) {
        super(name, email, "customer");
        this.password = password;
        this.status = status;
    }
	
	public String getStatus() {
		return status;
	}
	
	public String getPassword() {
        return password;
    }
}
