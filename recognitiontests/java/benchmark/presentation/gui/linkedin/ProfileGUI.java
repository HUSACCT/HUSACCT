package presentation.gui.linkedin;

import javax.swing.JFrame;

import presentation.exception.LinkedInException;
import domain.linkedin.LinkedInDTO;

//Functional requirement 4.4
//Test case 199: Exceptions in class presentation.gui.linkedin.ProfileGUI are only allowed when data is exchanged in datatype String
//Result: FALSE
public class ProfileGUI extends JFrame {
	public ProfileGUI(){
		LinkedInDTO transferAccount = new LinkedInDTO();
		transferAccount.setName("Mathew");
		transferAccount.setPassword("Schilke");	

		try {
			throw new LinkedInException(transferAccount);
		} catch (LinkedInException e) {
			e.printStackTrace();
		}
	}
}