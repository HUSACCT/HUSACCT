package domain.facebook;

//Functional requirement 3.1.2
//Test case 68: Only class presentation.gui.facebook.ChatGUI may have a dependency with domain.facebook.Message 
//Result: FALSE
public class Message {
	public static String getMessage(){
		return "chatmessage";
	}
}