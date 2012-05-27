package presentation.exception;

import domain.linkedin.LinkedInDTO;

public class LinkedInException extends Exception {

	public LinkedInException(LinkedInDTO transferAccount) {
		super("name " + transferAccount.getName() + " password: " + transferAccount.getPassword());
	}

	public LinkedInException(String message) {
		super(message);
	}
}