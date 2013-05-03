package husacct.control.task.configuration;

import javax.swing.JPanel;

public class ConfigurationScreen {

	private String name;
	private JPanel configPanel;
	
	public ConfigurationScreen(String name, JPanel configPanel) {
		this.name = name;
		this.configPanel = configPanel;
	}
	
	public String getName() {
		return name;
	}
	
	public JPanel getConfigPanel() {
		return configPanel;
	}
}
