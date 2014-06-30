package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.EventDAO;

//Functional requirement 3.2
//Test case 123: Class domain.locationbased.latitude.Event is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.EventDAO  
//Result: TRUE
public class Event {
	//FR5.5	
	private EventDAO dao;

	public Event(){
		//FR5.2
		System.out.println(dao.name);
	}
}