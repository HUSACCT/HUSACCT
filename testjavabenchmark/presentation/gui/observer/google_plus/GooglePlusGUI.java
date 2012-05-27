package presentation.gui.observer.google_plus;

//Functional requirement 4.1
//Test case 190: The class presentation.gui.observer.google_plus.GooglePlusGUI is not allowed to use the classes in package domain.google_plus except when there is an observer pattern that gives a notification.
//Result: TRUE
public class GooglePlusGUI implements Observer{
	
	@Override
	public void update() {
		refreshGUI();
	}

	private void refreshGUI() {
		
	}
}