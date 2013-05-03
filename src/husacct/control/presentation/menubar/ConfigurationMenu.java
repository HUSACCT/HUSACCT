package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ConfigurationMenu extends JMenuItem {

	private MainController mainController;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ConfigurationMenu(MainController mainController) {
		super("Configuration");
		this.mainController = mainController;
		setText(localeService.getTranslatedString("Configuration"));
		setListeners();
	}
	
	public void setListeners() {
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mainController.getApplicationController().showConfigurationGUI();
			}
		});
	}	
}
