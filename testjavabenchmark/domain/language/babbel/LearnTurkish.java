package domain.language.babbel;

import infrastructure.asocialmedia.ASocialMediaException;

//Functional requirement 3.1.3
//Test case 111: Class domain.language.babbel.LearnTurkish must use class infrastructure.socialmedia.SocialMediaException 
//Result: FALSE
public class LearnTurkish {
	public LearnTurkish(){
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