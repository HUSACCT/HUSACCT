package presentation.gui.observer.hyves;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
//Functional requirement 4.1
//Test case 191: The class presentation.gui.observer.hyves.HyvesMediaGUI is not allowed to use the  classes in package domain.hyves except when there is an observer pattern that gives a notification   
//Result: TRUE
public class HyvesMediaGUI extends JFrame implements Observer {
	public void update(Observable o, Object arg) {
		refreshGUI();		
	}

	private void refreshGUI(){

	}
}