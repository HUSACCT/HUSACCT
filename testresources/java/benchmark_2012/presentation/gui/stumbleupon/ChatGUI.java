package presentation.gui.stumbleupon;

import domain.stumbleupon.Message;
//Functional requirement 3.2.2
//Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
//Result: TRUE
public class ChatGUI {
	public ChatGUI(){
		//FR5.1
		System.out.println(Message.getMessage());
	}
}