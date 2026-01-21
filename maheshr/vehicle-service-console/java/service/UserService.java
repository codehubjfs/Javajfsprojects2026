package service;

import dao.UserDAO;
import exception.AuthenticationException;
import model.User;
import util.ValidationUtil;
import java.sql.SQLException;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public User login(String email, String password) throws AuthenticationException {
        try {
            if (!ValidationUtil.isValidEmail(email)) {
                throw new AuthenticationException("Invalid email format");
            }
            
            User user = userDAO.findByEmail(email);
            
            if (user == null) {
                throw new AuthenticationException("Invalid email or password");
            }
            
            if (!password.equals(user.getPassword())) {
                throw new AuthenticationException("Invalid email or password");
            }
            
            userDAO.updateLastLogin(user.getUserId());
            return user;
            
        } catch (SQLException e) {
            throw new AuthenticationException("Database error: " + e.getMessage());
        }
    }
    
    public User register(String fullName, String email, String password, String phone) 
            throws AuthenticationException {
        try {
            if (!ValidationUtil.isValidEmail(email)) {
                throw new AuthenticationException("Invalid email format");
            }
            
            if (!ValidationUtil.isValidPhone(phone)) {
                throw new AuthenticationException("Invalid phone number (must be 10 digits)");
            }
            
            User existingUser = userDAO.findByEmail(email);
            if (existingUser != null) {
                throw new AuthenticationException("Email already registered");
            }
            
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);

            user.setPassword(password);

            user.setPhone(phone);
            user.setRole("CUSTOMER");
            
            return userDAO.insert(user);
            
        } catch (SQLException e) {
            throw new AuthenticationException("Registration failed: " + e.getMessage());
        }
    }
}