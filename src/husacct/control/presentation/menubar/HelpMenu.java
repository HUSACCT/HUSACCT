package husacct.control.presentation.menubar;

import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	
	public HelpMenu(final MainController mainController){
		super("Help");
		
		JMenuItem versionItem = new JMenuItem("About HUSACCT");
		versionItem.setMnemonic('a');
		this.add(versionItem);
		versionItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showAboutHusacctGui();
			}
		});
	}
}
