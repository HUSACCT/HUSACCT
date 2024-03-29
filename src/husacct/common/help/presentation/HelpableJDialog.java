package husacct.common.help.presentation;

import java.awt.event.MouseEvent;

import husacct.common.help.Helpable;
import husacct.control.presentation.MainGui;

import javax.swing.*;

public class HelpableJDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	public HelpableJDialog(JFrame mainGui, boolean b) {
		super(mainGui, b);
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}

	public HelpableJDialog(JDialog dialogOwner, boolean b) {
		super(dialogOwner, b);
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public void doListener(MouseEvent e) {
		Helpable help = new Helpable(this);
		help.doListener(e);
	}
	
	
	
}
