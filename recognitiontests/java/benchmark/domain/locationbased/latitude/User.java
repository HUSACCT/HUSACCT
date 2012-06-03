package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.UserDAO;
//Functional requirement 3.2
//Test case 141: Class domain.locationbased.latitude.User is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.UserDAO
//Result: TRUE
public class User {
	public String sniName;
	public String sniMessage;
	private UserDAO userdao;
	
	public User(){
		//FR5.2
		//System.out.println(UserDAO.name);
	}
	
	public void testAccessFinalAttribute() {
		sniMessage = userdao.message;
	}
	
	public void testAccessStaticFinalAttribute() {
		sniName = UserDAO.name;
	}
}