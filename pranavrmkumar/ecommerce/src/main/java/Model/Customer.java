package Model;

public class Customer extends User{
	private String status;
	public Customer(String name,String email,String status) {
		super(name,email,"customer");
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
