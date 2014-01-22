package husacct.control.task.threading;

import java.util.Date;

import husacct.ServiceProvider;
import husacct.control.presentation.util.LoadingDialog;

import org.apache.log4j.Logger;

public class MonitorThread extends Thread implements Runnable {
	
	private Logger logger = Logger.getLogger(MonitorThread.class);
	
	private Thread taskThread;
	private LoadingDialog loadingDialog;
	
	public MonitorThread(Thread taskThread, LoadingDialog loadingDialog){
		this.taskThread = taskThread;
		this.loadingDialog = loadingDialog;
	}
	
	@Override
	public void run() {
		try {
			taskThread.join();
			loadingDialog.dispose();
			ServiceProvider.getInstance().getControlService().finishPreAnalysing();
			logger.debug(new Date().toString() + " Starting: getDefineService().analyze()");
			ServiceProvider.getInstance().getDefineService().analyze();
			logger.debug(new Date().toString() + " Finished: getDefineService().analyze()");
			logger.debug(new Date().toString() + String.format(" Finished: thread [%s], removed loader", taskThread.getName()));
		} catch (InterruptedException exception){
			taskThread.interrupt();
			logger.debug(String.format("thread [%s] interupted", taskThread.getName()));
		}
		
	}
}
