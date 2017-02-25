package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.task.TaskServiceImpl;

import javax.swing.JInternalFrame;

public class GuiController {

	private final TaskServiceImpl task;
	private final ConfigurationServiceImpl configuration;
	private BrowseViolations browseViolations;
	private FilterViolations filterViolations;
	private ConfigurationUI configurationUI;

	public GuiController(TaskServiceImpl task, ConfigurationServiceImpl configuration) {
		this.task = task;
		this.configuration = configuration;
		subscribeToLocalChangeListener();
	}

	private void subscribeToLocalChangeListener() {
		ServiceProvider.getInstance().getLocaleService().addServiceListener(() -> {
            initializeAllScreens();
            reloadGUIText();
        });
	}

	private void reloadGUIText() {
		browseViolations.loadText();
		filterViolations.loadGUIText();
		configurationUI.loadAfterChange();
	}

	public JInternalFrame getBrowseViolationsGUI() {
		initializeBrowseViolations();
		browseViolations.validateNow();
		return browseViolations;
	}

	public JInternalFrame getConfigurationGUI() {
		initializeConfigurationUI();
		return configurationUI;
	}

	public void violationChanged() {
		if (browseViolations != null) {
			browseViolations.reloadViolationPanelsAfterChange();
		}
	}

	private void initializeAllScreens() {
		initializeBrowseViolations();
		initializeConfigurationUI();
		initializeFilterViolations();
	}

	private void initializeBrowseViolations() {
		if (browseViolations == null) {
			this.browseViolations = new BrowseViolations(task, configuration);
			configuration.attachViolationHistoryRepositoryObserver(this.browseViolations);
		}
	}

	private void initializeConfigurationUI() {
		if (configurationUI == null) {
			this.configurationUI = new ConfigurationUI(task);
		}
	}

	private void initializeFilterViolations() {
		if (filterViolations == null) {
			initializeBrowseViolations();
			this.filterViolations = new FilterViolations(task, browseViolations);
		}
	}
}