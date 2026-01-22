package Exceptions;

public class InvalidRole extends Exception{
	public InvalidRole() {
		super("Mismatch role for the employee");
	}
}
