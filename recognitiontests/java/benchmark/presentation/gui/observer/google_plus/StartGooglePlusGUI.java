package presentation.gui.observer.google_plus;

import domain.google_plus.Circle;
import domain.google_plus.Contact;

public class StartGooglePlusGUI {
	public StartGooglePlusGUI(){
		GooglePlusGUI gui = new GooglePlusGUI();

		Circle circle = new Circle();
		circle.attach(gui);

		Contact c1 = new Contact("CHenry");
		c1.attach(gui);
		circle.addContact(c1);
		c1.setName("Henry");	
	}
}