package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.VenueDAO;
//Functional requirement 3.2
//Test case 144: Class domain.locationbased.foursquare.Venue is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.VenueDAO
//Result: FALSE
public class Venue {
	//FR5.5
	public VenueDAO getVenues(){
		return null;
	}
}