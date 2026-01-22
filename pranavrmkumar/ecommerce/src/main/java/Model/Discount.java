package Model;

import java.sql.Date;

public class Discount {
	private int discount_id;
	private String promo_code;
	private double discount_percentage;
	private Date expiry_date;
	private String status;
	
	public Discount(int discount_id,String promo_code,double discount_percentage,Date expiry_date,String status) {
		this.discount_id = discount_id;
		this.promo_code = promo_code;
		this.discount_percentage = discount_percentage;
		this.expiry_date = expiry_date;
		this.status = status;
	}
	
	public int getDID() {
		return discount_id;
	}
	
	public String getCode() {
		return promo_code;
	}
	
	public double getDPT() {
		return discount_percentage;
	}
	
	public Date getExpDate() {
		return expiry_date;
	}
	
	public String getStatus() {
		return status;
	}
}
