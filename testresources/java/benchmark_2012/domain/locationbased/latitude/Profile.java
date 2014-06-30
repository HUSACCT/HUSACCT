package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.ProfileDAO;

//Functional requirement 3.2
//Test case 131: Class domain.locationbased.latitude.Profile is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.ProfileDAO 
//Result: TRUE
public class Profile {
	//FR5.5
	public String getProfileInformation(ProfileDAO dao){
		return "";
	}
}