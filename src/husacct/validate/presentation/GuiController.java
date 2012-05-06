package husacct.validate.presentation;

import java.util.Locale;

import javax.swing.JInternalFrame;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.validate.task.TaskServiceImpl;

public class GuiController {
	private final BrowseViolations browseViolations;
	private final FilterViolations filterViolations;
	private final ConfigurationUI configurationUI;
		
	public GuiController(TaskServiceImpl task){
		this.browseViolations = new BrowseViolations(task);
		this.filterViolations = new FilterViolations(task, browseViolations);
		this.configurationUI = new ConfigurationUI(task);
		
		subscribeToLocalChangeListener();
	}
	
	private void subscribeToLocalChangeListener() {
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				reloadGUIText();
			}
		});		
	}
	
	private void reloadGUIText(){
		browseViolations.loadGUIText();
		filterViolations.loadGUIText();
		configurationUI.loadGUIText();
	}

	public JInternalFrame getBrowseViolationsGUI(){
		return browseViolations;
	}

	public JInternalFrame getConfigurationGUI(){
		return configurationUI;
	}
}