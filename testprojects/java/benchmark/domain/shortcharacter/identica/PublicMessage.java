package domain.shortcharacter.identica;

import infrastructure.asocialmedia.ASocialMediaException;

//Functional requirement 3.1.1
//Test case 59: Class domain.shortcharacter.identica.PublicMessage may only have a dependency with class infrastructure.socialmedia.SocialMediaException 
//Result: FALSE
public class PublicMessage {
	public PublicMessage(){
		try {
			getMessages();
			
			//FR5.8
		} catch (ASocialMediaException e) {
			e.printStackTrace();
		}
	}
	
	//FR5.8
	public String[] getMessages() throws ASocialMediaException{
		//FR5.8
		throw new ASocialMediaException("No Messages");
	}
}