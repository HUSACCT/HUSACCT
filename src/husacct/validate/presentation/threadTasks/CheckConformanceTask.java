package husacct.validate.presentation.threadTasks;

import java.util.Date;

import husacct.ServiceProvider;
import org.apache.log4j.Logger;

public class CheckConformanceTask implements Runnable {

	private Logger logger = Logger.getLogger(CheckConformanceTask.class);

	public CheckConformanceTask() {
	}

	@Override
	public void run() {
		try {
			ServiceProvider.getInstance().getControlService().setValidating(true);
			Thread.sleep(1);
			this.logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
			ServiceProvider.getInstance().getValidateService().checkConformance();
			ServiceProvider.getInstance().getControlService().setValidating(false);
			this.logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}
	}
}