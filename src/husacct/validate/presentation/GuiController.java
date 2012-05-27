package husacct.validate.presentation;

import java.util.Locale;

import javax.swing.JInternalFrame;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.task.TaskServiceImpl;

public class GuiController {
	private final TaskServiceImpl task;
	private final ConfigurationServiceImpl configuration;
	private BrowseViolations browseViolations;
	private FilterViolations filterViolations;
	private ConfigurationUI configurationUI;

	public GuiController(TaskServiceImpl task, ConfigurationServiceImpl configuration){
		this.task = task;	
		this.configuration = configuration;
		subscribeToLocalChangeListener();
	}

	private void subscribeToLocalChangeListener() {
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				initializeAllScreens();
				reloadGUIText();
			}
		});		
	}

	private void reloadGUIText(){		
		browseViolations.loadAfterChange();
		filterViolations.loadGUIText();
		configurationUI.loadAfterChange();
	}

	public JInternalFrame getBrowseViolationsGUI(){
		initializeBrowseViolations();
		return browseViolations;
	}

	public JInternalFrame getConfigurationGUI(){
		initializeConfigurationUI();
		return configurationUI;
	}

	public void violationChanged(){
		if(browseViolations != null){
			browseViolations.loadAfterChange();
		}
	}

	private void initializeAllScreens(){
		initializeBrowseViolations();
		initializeConfigurationUI();
		initializeFilterViolations();
	}

	private void initializeBrowseViolations(){
		if(browseViolations == null){
			this.browseViolations = new BrowseViolations(task, configuration);
			configuration.attachViolationHistoryRepositoryObserver(this.browseViolations);
		}
	}

	private void initializeConfigurationUI(){
		if(configurationUI == null){
			this.configurationUI = new ConfigurationUI(task);
		}
	}

	private void initializeFilterViolations(){
		if(filterViolations == null){
			initializeBrowseViolations();
			this.filterViolations = new FilterViolations(task, browseViolations);
		}
	}
}