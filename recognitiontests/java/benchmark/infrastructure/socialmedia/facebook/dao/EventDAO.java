package infrastructure.socialmedia.facebook.dao;

import domain.facebook.Event;

//Functional requirement 3.1.2
//Test case 70: Only class presentation.gui.facebook.EventGUI may have a dependency with domain.facebook.Event
//Result: FALSE
public class EventDAO{
	//FR5.5
	private Event event;

	public EventDAO(){
		//FR5.1
		System.out.println(event.getEventType());
	}
}