package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Models.Job;
import Models.Notification;
import Models.UserProfile;
import dao.EmployerDAO;

public class EmployerServices {
	
	public void displayProfile(String email) {
		UserProfile user = new EmployerDAO().viewProfile(email);
		System.out.println(user+"\n");
	}
	
	
	
	public void viewNotification(int user_id) {
		List<Notification> notifications = new ArrayList<>();
		
		notifications = new EmployerDAO().viewNotification(user_id);
		
		if(notifications.isEmpty()) {
			System.out.println("No Notifications for today");
		}
		else {
			notifications.stream().forEach(notification -> System.out.println(notification));                             
		}
	}
	
	public void postJob(int companyId , String jobTitle,String description , String location , int experience , LocalDate posted_date , LocalDate deadline
			,int pay_id) {
		new EmployerDAO().postJob(companyId, jobTitle, description, location, experience, posted_date, deadline, pay_id);
	}
	
	public void viewPotedJob(int company_id) {
		List<Job> postedjobs = new ArrayList<>();
		
		postedjobs = new EmployerDAO().viewPostedJobs(company_id);
		
		if(postedjobs.isEmpty()) {
			System.out.println("Yet to Post");
		}
		else {
			postedjobs.stream().forEach(job -> System.out.println(job));
		}
		
		
		
	}
	
	public void requestForVerification(int user_id) {
		new EmployerDAO().requestForVerification(user_id);
		System.out.println("Redquest Send Successfully");
	}
}
