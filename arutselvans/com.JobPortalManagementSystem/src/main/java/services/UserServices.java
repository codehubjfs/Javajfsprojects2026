package services;

import java.util.List;

import dao.UserDAO;
import Models.User;
import Exceptions.EmailFormateException;
import Exceptions.PasswordFormateException;
import dao.UserDAO;

public class UserServices {

    public static void viewAllUsers() {
        List<User> users = new UserDAO().getAllUser(); 
        
        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }

        
        users.stream()
             .forEach(user -> System.out.println(
                     user.getId() + " | " +
                     user.getName() + " | " +
                     user.getRole()
             ));
    }

    public void validateEmailFormate(String email) throws EmailFormateException {
		if(!email.matches("[A-Za-z][A-Za-z0-9]+@[A-Za-z]+([.][a-z]+)+")) {
			throw new EmailFormateException("Email Id formate to be like user@gmail.com");
		}
	}
    
    
    public void validatePasswordFormate(String password) throws PasswordFormateException{
    	if(! password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$")) {
    		throw new PasswordFormateException("Password must contain uppercase, lowercase, digit, special character and be at least 8 characters long");
    	}
    }
}
