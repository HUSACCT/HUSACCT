package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ToolMenu extends JMenu {

	private MainController mainController;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	JMenuItem options;
	
	public ToolMenu(MainController mainController) {
		super();
		this.mainController = mainController;
		options = new JMenuItem();
		this.add(options);
		
		setListeners();
		setComponentText();
	}
	
	public void setListeners() {
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainController.getApplicationController().showConfigurationGUI();
			}
		});
		
		localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				setComponentText();
			}
		});
	}
	
	private void setComponentText() {
		setText(localeService.getTranslatedString("Tools"));
		options.setText(localeService.getTranslatedString("ToolsOptions"));
	}
}
