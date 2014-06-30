package presentation.gui.linkedin;

import presentation.exception.LinkedInException;
//Functional requirement 4.4
//Test case 201: Exceptions in class presentation.gui.linkedin.ContactGUI are only allowed when data is exchanged in Data Transfer Objects
//Result: FALSE
public class ContactGUI {
	public ContactGUI(){
		try {
			throw new LinkedInException("Error in homeGUI");
		} catch (LinkedInException e) {
			e.printStackTrace();
		}
	}
}