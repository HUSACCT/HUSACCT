package domain.shortcharacter.twitter;

import infrastructure.socialmedia.SocialMediaException;

//Functional requirement 3.1.1
//Test case 58: Class domain.shortcharacter.twitter.TwitterList may only have a dependency with class infrastructure.socialmedia.SocialMediaException 
//Result: TRUE

public class TwitterList {
	public TwitterList(){
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