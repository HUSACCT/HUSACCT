package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public HelpMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("Help"));
		JMenuItem versionItem = new JMenuItem(controlService.getTranslatedString("About"));
		versionItem.setMnemonic('a');
		this.add(versionItem);
		versionItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showAboutHusacctGui();
			}
		});
	}
}
