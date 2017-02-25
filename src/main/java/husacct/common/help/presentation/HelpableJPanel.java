package husacct.common.help.presentation;

import java.awt.event.MouseEvent;

import husacct.common.help.Helpable;
import husacct.control.presentation.MainGui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HelpableJPanel extends JPanel{

	
	public HelpableJPanel() {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());		
	}
	
	public HelpableJPanel(MainGui mainGui, boolean b) {
		
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}

	public HelpableJPanel(JDialog dialogOwner, boolean b) {
	
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public void doListener(MouseEvent e) {
		Helpable help = new Helpable(this);
		help.doListener(e);
	}
	
	
	
}
