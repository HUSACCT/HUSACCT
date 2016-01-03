package husacct.common.help.presentation;

import java.awt.event.MouseEvent;

import husacct.common.help.Helpable;
import husacct.control.presentation.MainGui;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;

public class HelpableJInternalFrame extends JInternalFrame{

	public HelpableJInternalFrame(MainGui mainGui, boolean b) {
		
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}

	public HelpableJInternalFrame(JDialog dialogOwner, boolean b) {
		
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public HelpableJInternalFrame() {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public void doListener(MouseEvent e) {
		Helpable help = new Helpable(this);
		help.doListener(e);
	}
	
	
	
}
