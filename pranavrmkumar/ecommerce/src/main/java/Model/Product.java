package Model;

public class Product {
	private int product_id;
	private int category_id;
	private String name;
	private String brand;
	private double price;
	private String description;
	private String image_url;
	private String status;
	
	public Product(int product_id,int category_id,String name,String brand,double price,String description,String image_url,String status) {
		this.product_id = product_id;
		this.category_id = category_id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.description = description;
		this.image_url = image_url;
		this.status = status;
	}
	
	public int getProductID() {
		return product_id;
	}

	public int getCategoryID() {
		return category_id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getURL() {
		return image_url;
	}
	
	public String getStatus() {
		return status;
	}
	


}
