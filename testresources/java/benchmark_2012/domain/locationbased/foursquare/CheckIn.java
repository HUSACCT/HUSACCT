package domain.locationbased.foursquare;

import infrastructure.socialmedia.locationbased.foursquare.CheckInDAO;
//Functional requirement 3.2
//Test case 122: Class domain.locationbased.foursquare.CheckIn is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CheckInDAO 
//Result: FALSE
public class CheckIn {
	public CheckIn(){
		//FR5.2
		System.out.println(CheckInDAO.currentLocation);
	}
}