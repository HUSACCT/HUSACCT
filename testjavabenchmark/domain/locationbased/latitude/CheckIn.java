package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.CheckInDAO;
//Functional requirement 3.2
//Test case 121: Class domain.locationbased.latitude.CheckIn is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CheckInDAO 
//Result: TRUE
public class CheckIn {
	public CheckIn(){
		//FR5.2
		System.out.println(CheckInDAO.currentLocation);
	}
}