package husacct.control.task.threading;

import java.util.Date;

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
			if (loadingDialog != null) {
				loadingDialog.dispose();
			}
			logger.info(new Date().toString() + String.format(" Finished: thread [%s], removed loader ", taskThread.getName()));
		} catch (Exception exception){
			taskThread.interrupt();
			logger.error(String.format("thread [%s] interupted", taskThread.getName()));
		}
		
	}
}
