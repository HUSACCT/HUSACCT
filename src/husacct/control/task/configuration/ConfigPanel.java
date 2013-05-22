package husacct.control.task.configuration;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ConfigPanel extends JPanel {

	public abstract void SaveSettings();
	public abstract void ResetSettings();
}
