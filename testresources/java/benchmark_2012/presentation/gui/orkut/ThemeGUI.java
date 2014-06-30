package presentation.gui.orkut;

import domain.orkut.OrkutException;

//Functional requirement 3.1.2
//Test case 87: Only class presentation.gui.orkut.ThemeGUI may have a dependency with domain.orkut.OrkutException
//Result: TRUE
public class ThemeGUI {
	public ThemeGUI(){
		try {
			getThemes();			
			//FR5.8
		} catch (OrkutException e) {
			e.printStackTrace();
		}
	}

	//FR5.8
	public String getThemes() throws OrkutException{
		//FR5.8
		throw new OrkutException("No themes available");
	}
}