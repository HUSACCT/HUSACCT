package husacct.control.presentation.viewcontrol;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class TaskBar extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public TaskBar(){
		setup();
	}
	
	public void addToggleButton(JToggleButton toggleButton) {
		add(toggleButton);
		validate();
		repaint();
	}
	
	public void removeToggleButton(JToggleButton toggleButton) {
		remove(toggleButton);
		validate();
		repaint();
	}

	private void setup(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}
}
