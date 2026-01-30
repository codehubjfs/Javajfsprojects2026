package ServiceLayer;

import DAO.AuthDAO;
import Exceptions.DBAccessException;
import Exceptions.InvalidCredentialsException;
import Model.User;

public class AuthService {
	
	public static User login(String email, String password)
	        throws InvalidCredentialsException, DBAccessException {
	    return AuthDAO.login(email, password);
	}

	
	
	public static boolean registerCustomer(
	        String name, String email, String password,
	        String street, String city, String state, String pincode, String address_type
	) throws DBAccessException {

	    // email already exists check
	    if (AuthDAO.emailExists(email)) {
	        throw new DBAccessException("Email already registered.");
	    }

	    return AuthDAO.registerCustomerWithAddress(
	            name, email, password,
	            street, city, state, pincode, address_type
	    );
	}

}
