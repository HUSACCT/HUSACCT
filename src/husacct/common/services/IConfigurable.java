package husacct.common.services;

import javax.swing.JPanel;

public interface IConfigurable {
	String getConfigurationName();
	JPanel getConfigurationPanel();
}
