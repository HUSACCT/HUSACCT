package husacct.common.help.presentation;

import husacct.common.help.Helpable;
import husacct.control.presentation.MainGui;

import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JTable;

public class HelpableJTable extends JTable{
	private static final long serialVersionUID = 1L;

	public HelpableJTable() {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());		
	}
	
	public HelpableJTable(MainGui mainGui, boolean b) {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}

	public HelpableJTable(JDialog dialogOwner, boolean b) {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public void doListener(MouseEvent e) {
		Helpable help = new Helpable(this);
		help.doListener(e);
	}
}