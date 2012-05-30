package domain.facebook;

//Functional requirement 3.1.2
//Test case 70: Only class presentation.gui.facebook.EventGUI may have a dependency with domain.facebook.Event
//Result: FALSE
public class Event {
	public String getEventType(){
		return "party";
	}
}