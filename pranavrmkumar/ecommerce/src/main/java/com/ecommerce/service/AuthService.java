package com.ecommerce.service;

import com.ecommerce.dao.AuthDAO;
import com.ecommerce.exceptions.DBAccessException;
import com.ecommerce.exceptions.InvalidCredentialsException;
import com.ecommerce.models.User;

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
