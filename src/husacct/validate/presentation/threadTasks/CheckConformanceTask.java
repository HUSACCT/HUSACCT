package husacct.validate.presentation.threadTasks;

import java.util.Date;

import javax.swing.SwingUtilities;

import husacct.ServiceProvider;
import husacct.validate.presentation.BrowseViolations;

import org.apache.log4j.Logger;

public class CheckConformanceTask implements Runnable {
	private BrowseViolations gui;
	private Logger logger = Logger.getLogger(CheckConformanceTask.class);

	public CheckConformanceTask(BrowseViolations gui) {
		this.gui = gui;
	}

	@Override
	public void run() {
		try {
			ServiceProvider.getInstance().getControlService().setValidating(true);
			Thread.sleep(1);
			this.logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
			ServiceProvider.getInstance().getValidateService().checkConformance();
			this.logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
			Thread.sleep(10);
			ServiceProvider.getInstance().getControlService().setValidating(false);
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}
		// Update the GUI afterwards
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.reloadViolationPanelsAfterChange();
			}
		});
	}
}