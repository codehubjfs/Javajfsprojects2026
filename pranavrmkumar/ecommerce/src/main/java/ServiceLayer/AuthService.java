package ServiceLayer;

import DAO.AuthDAO;
import Exceptions.DBAccessException;
import Exceptions.InvalidCredentialsException;
import Model.Admin;
import Model.Customer;

public class AuthService {
	
	//admin login
	public static Admin adminLogin(String email,String password) throws DBAccessException,InvalidCredentialsException{
		Admin admin = AuthDAO.adminLogin(email, password);
		
		if(admin == null) {
			throw new InvalidCredentialsException("Invalid Credentials");
		}
		return admin;
	}
	
	
	//Customer login
	public static Customer customerLogin(String email, String password)
            throws InvalidCredentialsException, DBAccessException {

        return AuthDAO.customerLogin(email, password);
    }
}
