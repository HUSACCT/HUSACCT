package domain.facebook;

//Functional requirement 3.1.2
//Test case 88: Only class presentation.gui.facebook.WallGUI may have a dependency with domain.facebook.FacebookException
//Result: FALSE
public class FacebookException extends Exception {
	public FacebookException(String message){
		super(message);
	}
}