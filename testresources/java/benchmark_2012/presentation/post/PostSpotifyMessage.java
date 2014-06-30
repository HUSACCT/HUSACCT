package presentation.post;

import infrastructure.socialmedia.SocialMediaException;
//Functional requirement 3.2.2
//Test case 166: The classes in package presentation.post are not allowed to use modules in a not direct lower layer
//Result: FALSE
public class PostSpotifyMessage {
	public PostSpotifyMessage(){
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