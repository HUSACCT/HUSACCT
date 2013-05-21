package husacct.common.help;

import husacct.ServiceProvider;
import husacct.control.presentation.util.HelpDialog;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class HelpMouseListener implements MouseListener{

	Component component;

	public HelpMouseListener(Component comp) {
		this.component = comp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		popUpMenu(e);
	}
	
	private void popUpMenu(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			JPopupMenu popup = new JPopupMenu();
			JMenuItem menu = new JMenuItem("Help");
			menu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ServiceProvider.getInstance().getControlService().showHelpDialog(component);					
				}
				
			});
			popup.add(menu);
			popup.show(component, e.getX(), e.getY());
		}
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}



}
