package husacct.control.presentation.util;

import husacct.control.task.MainController;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {

	
	public ConfigurationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
	}
}
