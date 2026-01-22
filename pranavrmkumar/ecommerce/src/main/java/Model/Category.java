package Model;

public class Category {
	private int category_id;
	private String category_name;
	private String description;
	private String status;
	
	public Category(int category_id,String category_name,String description,String status) {
		this.category_id = category_id;
		this.category_name = category_name;
		this.description = description;
		this.status = status;
	}
	
	public int getCategoryID() {
		return category_id;
	}
	
	public String getCategoryName() {
		return category_name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStatus() {
		return status;
	}
}
