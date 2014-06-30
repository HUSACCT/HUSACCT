package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.BadgesDAO;
//Functional requirement 3.2
//Test case 118: Class domain.locationbased.foursquare.Badges is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.BadgesDAO 
//Result: FALSE
public class Badges {
	public Badges(){
		//FR5.1
		System.out.println(BadgesDAO.getAllBadges());
	}
}