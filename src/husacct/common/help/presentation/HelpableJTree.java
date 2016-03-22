package husacct.common.help.presentation;

import husacct.common.help.Helpable;
import husacct.control.presentation.MainGui;

import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class HelpableJTree extends JTree {
	private static final long serialVersionUID = 1L;

	public HelpableJTree() {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());		
	}
	
	public HelpableJTree(MainGui mainGui, boolean b) {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}

	public HelpableJTree(JDialog dialogOwner, boolean b) {
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public HelpableJTree(DefaultMutableTreeNode root){
		super(root);
		Helpable help = new Helpable(this);
		addMouseListener(help.getListener());
	}
	
	public void doListener(MouseEvent e) {
		Helpable help = new Helpable(this);
		help.doListener(e);
	}
}