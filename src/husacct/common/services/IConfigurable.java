package husacct.common.services;

import java.util.HashMap;

import husacct.control.task.configuration.ConfigPanel;

public interface IConfigurable {
	String getConfigurationName();
	ConfigPanel getConfigurationPanel();
	HashMap<String, ConfigPanel> getSubItems();
}
