package com.ecommerce.exceptions;

public class EntityNotFoundException extends AppException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String message) {
		super(message);
	}
}
