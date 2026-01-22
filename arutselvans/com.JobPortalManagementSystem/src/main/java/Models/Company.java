package Models;

public class Company {
	private int companyId;
	private String companyName;
	private String location;
	private String description;
	
	public Company(int companyId,String companyName,String location,String description) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.location = location;
		this.description = description;
	}
	
	public String toString() {
		return  "Company Id : "+this.companyId+" | "+
				"Company Name : "+this.companyName+" | "+
				"Company Location : "+this.location+" | "+
				"Descriptin : "+this.description;
	}
	
	public String getLocation() { return location; }
}
