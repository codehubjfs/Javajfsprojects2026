package service;

import model.User;
import exception.RegistrationException;
import exception.NotFoundException;
import exception.ServiceException;
import enums.UserStatus;

import java.util.List;

public interface UserService {

    // Admin operations
    int registerUser(User user) throws RegistrationException;
    List<User> getUsersByRole(int roleId) throws ServiceException;
    boolean updateUserStatus(int userId, UserStatus status) throws ServiceException;
    User getUserById(int userId) throws NotFoundException;
    User getUserByEmail(String email) throws NotFoundException;
    boolean updateUserProfile(User user) throws ServiceException; // allows user to update profile details (except ID)
    List<User> getAllUsers() throws ServiceException; // view all users
}
