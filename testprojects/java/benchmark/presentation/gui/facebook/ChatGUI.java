package presentation.gui.facebook;

import domain.facebook.Message;

//Functional requirement 3.1.2
//Test case 68: Only class presentation.gui.facebook.ChatGUI may have a dependency with domain.facebook.Message 
//Result: FALSE
public class ChatGUI {
	public ChatGUI(){
		//FR5.1
		System.out.println(Message.getMessage());
	}
}