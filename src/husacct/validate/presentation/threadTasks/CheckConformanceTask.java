package husacct.validate.presentation.threadTasks;

import husacct.ServiceProvider;

import org.apache.log4j.Logger;

public class CheckConformanceTask implements Runnable {
	
	private Logger logger = Logger.getLogger(CheckConformanceTask.class);
	
	@Override
	public void run() {
		try {
			Thread.sleep(1);
			ServiceProvider.getInstance().getValidateService().checkConformance();
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}		
	}
}