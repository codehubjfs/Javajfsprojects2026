package Model;

import java.sql.Timestamp;

public class Ticket {
	private int ticket_id;
	private int user_id;
	private int order_id;
	private String issue_type;
	private String description;
	private String ticket_status;
	private Timestamp created_date;
	
	public Ticket(int ticket_id,int user_id,int order_id,String issue_type,String description,String ticket_status,Timestamp created_date) {
		this.ticket_id = ticket_id;
		this.user_id = user_id;
		this.order_id = order_id;
		this.issue_type = issue_type;
		this.description = description;
		this.ticket_status = ticket_status;
		this.created_date = created_date;
	}
	
	public int getTID() {
		return ticket_id;
	}
	
	public int getUID() {
		return user_id;
	}
	
	public int getOID() {
		return order_id;
	}
	
	
	public String getIssueType() {
		return issue_type;
	}
	
	public String getDesc() {
		return description;
	}
	
	public String getTicketStatus() {
		return ticket_status;
	}
	
	public Timestamp getDate() {
		return created_date;
	}
}
