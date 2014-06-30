package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.UserDAO;
//Functional requirement 3.2
//Test case 142: Class domain.locationbased.foursuare.User is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.UserDAO
//Result: FALSE
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