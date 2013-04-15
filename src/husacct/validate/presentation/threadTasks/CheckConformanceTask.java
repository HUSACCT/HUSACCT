package husacct.validate.presentation.threadTasks;

import husacct.ServiceProvider;
import husacct.validate.presentation.browseViolations.FilterPanel;

import javax.swing.JButton;

import org.apache.log4j.Logger;

public class CheckConformanceTask implements Runnable {

	private final FilterPanel filterPanel;
	private final JButton buttonSaveInHistory;
	private Logger logger = Logger.getLogger(CheckConformanceTask.class);

	public CheckConformanceTask(FilterPanel filterPanel, JButton buttonSaveInHistory) {
		this.filterPanel = filterPanel;
		this.buttonSaveInHistory = buttonSaveInHistory;
	}

	@Override
	public void run() {
		try {
			ServiceProvider.getInstance().getControlService().setValidate(true);
			Thread.sleep(1);
			ServiceProvider.getInstance().getValidateService().checkConformance();
			filterPanel.loadAfterChange();
			buttonSaveInHistory.setEnabled(true);
			ServiceProvider.getInstance().getControlService().setValidate(false);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}
	}
}