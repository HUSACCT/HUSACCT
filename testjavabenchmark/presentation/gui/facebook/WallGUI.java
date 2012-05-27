package presentation.gui.facebook;

import domain.facebook.FacebookException;

//Functional requirement 3.1.2
//Test case 88: Only class presentation.gui.facebook.WallGUI may have a dependency with domain.facebook.FacebookException
//Result: FALSE
public class WallGUI {
	public WallGUI(){
		try {
			getWall();			
			//FR5.8
		} catch (FacebookException e) {
			e.printStackTrace();
		}
	}

	//FR5.8
	public String getWall() throws FacebookException{
		//FR5.8
		throw new FacebookException("No Wall available");
	}
}