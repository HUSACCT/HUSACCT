package husacct.validate.presentation.threadTasks;

import husacct.ServiceProvider;
import husacct.validate.presentation.BrowseViolations;
import husacct.validate.presentation.browseViolations.FilterPanel;

import javax.swing.JButton;

import org.apache.log4j.Logger;

public class CheckConformanceTask implements Runnable {
	
	private final BrowseViolations browseViolations;
	private final FilterPanel filterPanel;
	private final JButton buttonSaveInHistory;
	
	private Logger logger = Logger.getLogger(CheckConformanceTask.class);
	
	public CheckConformanceTask(BrowseViolations browseViolations, FilterPanel filterPanel, JButton buttonSaveInHistory) {
		this.browseViolations = browseViolations;
		this.filterPanel = filterPanel;
		this.buttonSaveInHistory = buttonSaveInHistory;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1);
			ServiceProvider.getInstance().getValidateService().checkConformance();
			browseViolations.loadAfterChange();
			filterPanel.loadAfterChange();
			buttonSaveInHistory.setEnabled(true);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}		
	}
}