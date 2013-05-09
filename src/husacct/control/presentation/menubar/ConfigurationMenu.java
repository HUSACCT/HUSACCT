package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ConfigurationMenu extends JMenu {

	private MainController mainController;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ConfigurationMenu(MainController mainController) {
		super("Configuration");
		this.mainController = mainController;
		setText(localeService.getTranslatedString("Configuration"));
		setListeners();
		this.add(new JMenuItem(""));
	}
	
	public void setListeners() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainController.getApplicationController().showConfigurationGUI();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		final ConfigurationMenu configurationMenu = this;
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				configurationMenu.setText(localeService.getTranslatedString("Configuration"));
			}
		});
	}	
}
