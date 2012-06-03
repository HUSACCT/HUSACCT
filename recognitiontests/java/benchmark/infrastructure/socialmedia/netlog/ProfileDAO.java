package infrastructure.socialmedia.netlog;

import infrastructure.socialmedia.SocialMediaException;
//Functional requirement 3.2.1
//Test case 160: All classes in package infrastructure.socialmedia.netlog are not allowed to use modules in a higher layer 
//Result: TRUE
public class ProfileDAO {
	public ProfileDAO(){
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