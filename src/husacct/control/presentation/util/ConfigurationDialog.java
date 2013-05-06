package husacct.control.presentation.util;

import java.awt.Dimension;
import java.awt.FlowLayout;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ConfigurationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		setTitle(localeService.getTranslatedString("Configuration"));
		initiliaze();
		this.setVisible(true);
	}
	
	public void initiliaze() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(420, 380));
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}
}
