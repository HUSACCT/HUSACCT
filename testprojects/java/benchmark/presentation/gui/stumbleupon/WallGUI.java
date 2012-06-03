package presentation.gui.stumbleupon;

import domain.stumbleupon.StumbleException;
//Functional requirement 3.2.2
//Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
//Result: TRUE
public class WallGUI {
	public WallGUI(){
		try {
			getWall();			
			//FR5.8
		} catch (StumbleException e) {
			e.printStackTrace();
		}
	}

	//FR5.8
	public String getWall() throws StumbleException{
		//FR5.8
		throw new StumbleException("No Wall available");
	}
}