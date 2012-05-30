package domain.language.busuu;

import infrastructure.socialmedia.SocialMediaException;

//Functional requirement 3.1.3
//Test case 112: Class domain.language.busuu.LearnTurkish must use class infrastructure.socialmedia.SocialMediaException 
//Result: TRUE
public class LearnTurkish {
	public LearnTurkish(){
		try {
			getList();			
			//FR5.8
		} catch (SocialMediaException e) {
			e.printStackTrace();
		}
	}
	
	//FR5.8
	public String[] getList() throws SocialMediaException{
		//FR5.8
		throw new SocialMediaException("No Followers");
	}
}