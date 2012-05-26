package husacct.validate.presentation.threadTasks;

import husacct.control.task.AnalyseTask;
import husacct.validate.presentation.BrowseViolations;

import org.apache.log4j.Logger;

public class BrowseViolationsUpdateTask implements Runnable{
	private Logger logger = Logger.getLogger(AnalyseTask.class);
	private final BrowseViolations browseViolations;

	public BrowseViolationsUpdateTask(BrowseViolations browseViolations) {
		this.browseViolations = browseViolations;
	}	
	
	@Override
	public void run() {
		try {
			Thread.sleep(1);
			browseViolations.updateGuiWithViolationHistory();
		} catch (InterruptedException exception){
			logger.debug("Analyse interupted");
		}
	}
}
