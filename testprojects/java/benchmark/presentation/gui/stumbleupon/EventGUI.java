package presentation.gui.stumbleupon;

import domain.stumbleupon.Event;
//Functional requirement 3.2.2
//Test case 165: All classes in package presentation.gui.stumbleupon are not allowed to use in a not direct lower layer
//Result: TRUE
public class EventGUI{
	//FR5.5
	private Event event;

	public EventGUI(){
		//FR5.1
		System.out.println(event.getEventType());
	}
}