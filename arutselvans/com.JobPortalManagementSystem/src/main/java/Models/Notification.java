package Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Notification {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private String message;
	private LocalDate date;
	private int sender_id;
	private int receiver_id;
	
	public Notification(String message, LocalDate date,int sender_id , int receiver_id) {
		this.setMessage(message);
		this.setDate(date);
		this.receiver_id = receiver_id;
		this.sender_id = sender_id;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public String toString() {
		return "Message: "+this.message+"\n"+
				"Date: "+this.date.format(formatter)+"\n"+
				"------------------------------------------";
	}

	/**
	 * @return the sender_id
	 */
	public int getSender_id() {
		return sender_id;
	}

	/**
	 * @param sender_id the sender_id to set
	 */
	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}

	/**
	 * @return the receiver_id
	 */
	public int getReceiver_id() {
		return receiver_id;
	}

	/**
	 * @param receiver_id the receiver_id to set
	 */
	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
	}
}
