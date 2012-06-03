package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.ProfileDAO;

//Functional requirement 3.2
//Test case 132: Class domain.locationbased.foursquare.Profile is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.ProfileDAO 
//Result: FALSE
public class Profile {
	//FR5.5
	public String getProfileInformation(ProfileDAO dao){
		return "";
	}
}