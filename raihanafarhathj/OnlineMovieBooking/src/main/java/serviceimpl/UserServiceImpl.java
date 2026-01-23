package serviceimpl;

import service.UserService;
import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import exception.RegistrationException;
import exception.NotFoundException;
import exception.ServiceException;
import enums.UserStatus;

import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    // ------------------- Admin Operations -------------------

    @Override
    public int registerUser(User user) throws RegistrationException {
        try {
            validateUserForRegistration(user);
            return userDAO.addUser(user);
        } catch (Exception ex) {
            throw new RegistrationException("Failed to register user: " + ex.getMessage());
        }
    }

    @Override
    public List<User> getUsersByRole(int roleId) throws ServiceException {
        try {
            return userDAO.getUsersByRole(roleId)
                    .stream()
                    .filter(u -> u != null)
                    .sorted((u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching users by role: " + ex.getMessage());
        }
    }

    @Override
    public boolean updateUserStatus(int userId, UserStatus status) throws ServiceException {
        try {
            return userDAO.updateUserStatus(userId, status.name());
        } catch (Exception ex) {
            throw new ServiceException("Error updating user status: " + ex.getMessage());
        }
    }

    @Override
    public User getUserById(int userId) throws NotFoundException {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) throw new NotFoundException("User with ID " + userId + " not found.");
            return user;
        } catch (Exception ex) {
            throw new NotFoundException("Error fetching user: " + ex.getMessage());
        }
    }

    // ------------------- Customer Operations -------------------

    @Override
    public User getUserByEmail(String email) throws NotFoundException {
        try {
            User user = userDAO.getUserByEmail(email);
            if (user == null) throw new NotFoundException("User with email " + email + " not found.");
            return user;
        } catch (Exception ex) {
            throw new NotFoundException("Error fetching user by email: " + ex.getMessage());
        }
    }

    @Override
    public boolean updateUserProfile(User user) throws ServiceException {
        try {
            if (user == null || user.getUserId() <= 0) {
                throw new ServiceException("Invalid user details.");
            }
            User existingUser = userDAO.getUserById(user.getUserId());
            if (existingUser == null) throw new ServiceException("User not found.");

            // Update only modifiable fields
            if (user.getName() != null && !user.getName().isBlank()) existingUser.setName(user.getName());
            if (user.getPhoneNumber() != null && !user.getPhoneNumber().isBlank())
                existingUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getDob() != null) existingUser.setDob(user.getDob());

            // You can add email/password updates here with validation if needed

            return userDAO.updateUserStatus(existingUser.getUserId(), existingUser.getStatus().name());
        } catch (Exception ex) {
            throw new ServiceException("Error updating profile: " + ex.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDAO.getUsersByRole(0) // 0 or a special method in DAO to fetch all users
                    .stream()
                    .filter(u -> u != null)
                    .sorted((u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServiceException("Error fetching all users: " + ex.getMessage());
        }
    }

    // ------------------- Validation -------------------
    private void validateUserForRegistration(User user) throws RegistrationException {
        if (user == null) throw new RegistrationException("User cannot be null.");
        if (user.getName() == null || user.getName().isBlank())
            throw new RegistrationException("Name cannot be empty.");
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new RegistrationException("Email cannot be empty.");
        if (user.getPassword() == null || user.getPassword().isBlank())
            throw new RegistrationException("Password cannot be empty.");
        if (user.getDob() == null) throw new RegistrationException("Date of birth cannot be empty.");
    }
}
