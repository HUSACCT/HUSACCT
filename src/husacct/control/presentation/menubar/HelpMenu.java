package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	
	private JMenuItem aboutItem;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public HelpMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("Help"));
		aboutItem = new JMenuItem(controlService.getTranslatedString("About"));
		aboutItem.setMnemonic('a');
		this.add(aboutItem);
		aboutItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showAboutHusacctGui();
			}
		});
		
		final HelpMenu helpMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				helpMenu.setText(controlService.getTranslatedString("Help"));
				aboutItem.setText(controlService.getTranslatedString("About"));
				
			}
		});
		
	}
}
