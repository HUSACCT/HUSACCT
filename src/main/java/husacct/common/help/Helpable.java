package husacct.common.help;


import husacct.ServiceProvider;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;

public  class Helpable{
	Component component;
	public Helpable(Component comp) {

		this.component = comp;
	}
	
	public MouseListener getListener() {
		
		return new HelpMouseListener(this.component);
		
	}
	
	public void doListener(MouseEvent e) {
		new HelpMouseListener(this.component).mouseClicked(e);
	}
	
	public JMenuItem getHelpItem() {
		JMenuItem menu = new JMenuItem("Help");
		menu.addActionListener(e -> ServiceProvider.getInstance().getControlService().showHelpDialog(component));
		return menu;
	}

}
