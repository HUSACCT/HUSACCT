package husacct.control.task;

import husacct.ServiceProvider;

import org.apache.log4j.Logger;

public class AnalyseTask implements Runnable{

	private Logger logger = Logger.getLogger(AnalyseTask.class);
	
	@Override
	public void run() {
		// Thread.sleep added to support InterruptedException catch
		// InterruptedException is not yet implemented by analyse
		// Therefor this thread can never be interrupted.
		try {
			Thread.sleep(1);
			logger.debug("Analysing application");
			ServiceProvider.getInstance().getAnalyseService().analyseApplication();
			logger.debug("Application analysed");
		} catch (InterruptedException exception){
			logger.debug("Analyse interupted");
		}
	}

}
