package dao;

import model.Role;
import exception.DataAccessException;
import java.util.List;

public interface RoleDAO {
    Role getRoleById(int roleId) throws DataAccessException;
    Role getRoleByName(String roleName) throws DataAccessException;
    List<Role> getAllRoles() throws DataAccessException;
}
