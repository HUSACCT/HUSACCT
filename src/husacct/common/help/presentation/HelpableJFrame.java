package husacct.common.help.presentation;


import java.awt.event.MouseEvent;

import husacct.common.help.Helpable;

import javax.swing.JFrame;

public class HelpableJFrame extends JFrame{

	public HelpableJFrame() {
		super();
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}

	public void doListener(MouseEvent e) {
		Helpable help = new Helpable(this);
		help.doListener(e);
	}
	
	
}
