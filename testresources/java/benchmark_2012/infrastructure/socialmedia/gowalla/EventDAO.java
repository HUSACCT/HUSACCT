package infrastructure.socialmedia.gowalla;

import domain.gowalla.Event;
//Functional requirement 3.2.1
//Test case 161: The classes in package infrastructure.socialmedia.gowalla are not allowed to use modules in a higher layer
//Result: FALSE
public class EventDAO {
	public EventDAO(){
		//FR5.2
		System.out.println(Event.linkUrl);
	}
}