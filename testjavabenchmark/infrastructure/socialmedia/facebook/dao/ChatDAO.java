package infrastructure.socialmedia.facebook.dao;

import domain.facebook.Message;

//Functional requirement 3.1.2
//Test case 68: Only class presentation.gui.facebook.ChatGUI may have a dependency with domain.facebook.Message 
//Result: FALSE
public class ChatDAO {
	public ChatDAO(){
		//FR5.1
		System.out.println(Message.getMessage());
	}
}