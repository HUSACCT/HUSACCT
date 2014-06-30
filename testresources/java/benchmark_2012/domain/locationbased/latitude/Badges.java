package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.BadgesDAO;

//Functional requirement 3.2
//Test case 117: Class domain.locationbased.latitude.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
//Result: TRUE
public class Badges {
	public Badges(){
		//FR5.1
		System.out.println(BadgesDAO.getAllBadges());
	}
}