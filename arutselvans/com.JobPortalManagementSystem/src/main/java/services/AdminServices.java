package services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Models.Company;
import Models.Job;
import Models.Notification;
import Models.User;
import dao.UserDAO;




public class AdminServices {

	public void displayuserBasedOnGender() {
		List<User> users = new UserDAO().getAllUser();
		if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }
		  Map<String, List<User>> genderBased =
	                users.stream()
	                     .collect(Collectors.groupingBy(User::getGender));
	        for (Map.Entry<String, List<User>> entry : genderBased.entrySet()) {

	            System.out.println("Gender: " + entry.getKey());

	            for (User user : entry.getValue()) {
	                System.out.println(user);
	            }
	        }
	}
	public void sortBasedOnDateOfBirth() {
		List<User> users = new UserDAO().getAllUser();
		if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }
        users.stream().sorted(Comparator.comparing(User::getDateOfBirth).reversed()).forEach(System.out::println);

	}
	public void displayBasedOnUserRole() {
		List<User> users = new UserDAO().getAllUser();
		if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }
		  Map<String,List<User>> rolebased = users.stream().collect(Collectors.groupingBy(User::getRole));
	        
	        for(Map.Entry<String, List<User>> entry : rolebased.entrySet()) {
	        	System.out.println();
	        	System.out.println("Role :"+entry.getKey());
	        	for(User user : entry.getValue()) {
	        		System.out.println(user);
	        	}
	        	System.out.println();
	        }
	}
	public void viewAllJobs() {
		List<Job> jobs = new ArrayList<>(); 
		
		jobs = new UserDAO().getAllJobs();
		
		jobs.stream().forEach(job -> System.out.println(job));
	}
	
	public void viewAllCompanies() {
		List<Company> companies = new ArrayList<>();
		
		companies = new UserDAO().viewAllCompanies();
		
		companies.stream().forEach(company -> System.out.println(company));
	}
	public void blockuser(int user_id) {
		new UserDAO().blockUser(user_id);
		System.out.println("User Blocked Successfully");
	}
	
	public void viewAllUsers() {
        List<User> users = new UserDAO().getAllUser(); 
        
        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }

        users.stream().sorted(Comparator.comparing(User::getName)).forEach(System.out::println);
      
        
      


    }
	
	public void displayNotification(int user_id) {
		List<Notification> notifications = new ArrayList<>();
		notifications = new UserDAO().viewNotification(user_id);

		if(notifications.isEmpty()) {
			System.out.println("No notifications");
		}
		else {
			notifications.stream().forEach(notification -> System.out.println(notification));
		}
	}
	
	public void verifyEMployer(int employer_id) {
		new UserDAO().verifyEmployer(employer_id);
	}
	
	
	public void sendAnnounceMent(String message) {
		new UserDAO().sendAnnouncement(message);
	}
	
}
