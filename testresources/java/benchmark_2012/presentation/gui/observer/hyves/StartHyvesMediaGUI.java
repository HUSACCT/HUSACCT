package presentation.gui.observer.hyves;

import domain.hyves.Krabbel;

public class StartHyvesMediaGUI {
	public StartHyvesMediaGUI(){
		HyvesMediaGUI gui = new HyvesMediaGUI();
		Krabbel krabbel = new Krabbel();

		krabbel.setText("text message");

		krabbel.addObserver(gui);

		krabbel.setText("text changed");
	}
}