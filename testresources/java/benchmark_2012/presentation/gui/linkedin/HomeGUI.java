package presentation.gui.linkedin;

import presentation.exception.LinkedInException;
//Functional requirement 4.4
//Test case 198: Exceptions in class presentation.gui.linkedin.HomeGUI are only allowed when data is exchanged in datatype String
//Result: TRUE
public class HomeGUI {
	public HomeGUI(){
		try {
			throw new LinkedInException("Error in homeGUI");
		} catch (LinkedInException e) {
			e.printStackTrace();
		}
	}
}