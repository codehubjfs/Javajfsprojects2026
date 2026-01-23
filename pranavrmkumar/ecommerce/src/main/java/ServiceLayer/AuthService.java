package ServiceLayer;

import DAO.AuthDAO;
import Exceptions.DataAccessException;
import Exceptions.InvalidCredentialsException;
import Model.User;

public class AuthService {
	
	//admin login
	public static User adminLogin(String email,String password) throws DataAccessException,InvalidCredentialsException{
		User admin = AuthDAO.adminLogin(email, password);
		
		if(admin == null) {
			throw new InvalidCredentialsException("Invalid Credentials");
		}
		return admin;
	}
}
