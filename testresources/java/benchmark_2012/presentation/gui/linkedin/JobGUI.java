package presentation.gui.linkedin;

import javax.swing.JFrame;

import presentation.exception.LinkedInException;
import domain.linkedin.LinkedInDTO;
//Functional requirement 4.4
//Test case 200: Exceptions in class presentation.gui.linkedin.JobGUI are only allowed when data is exchanged in Data Transfer Objects
//Result: TRUE
public class JobGUI extends JFrame {

	public JobGUI(){
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