package dao;

import model.User;
import exception.DataAccessException;
import java.util.List;

public interface UserDAO {
    int addUser(User user) throws DataAccessException;
    User getUserById(int userId) throws DataAccessException;
    User getUserByEmail(String email) throws DataAccessException;
    List<User> getUsersByRole(int roleId) throws DataAccessException;
    boolean updateUserStatus(int userId, String status) throws DataAccessException;
}
