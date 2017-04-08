package husacct.control.task.threading;

import husacct.ServiceProvider;
import husacct.control.presentation.util.LoadingDialog;
import husacct.control.task.MainController;

public class ThreadWithLoader implements Runnable {
	
	private static int taskcounter = 0;
	
	private LoadingDialog loadingDialog;
	private Thread taskThread;
	private Thread monitorThread;
	
	public ThreadWithLoader(MainController mainController, String progressInfoText, Runnable threadTask){
		int taskCounter = ThreadWithLoader.taskcounter++;
		if (ServiceProvider.getInstance().getControlService().isGuiEnabled()) {
			loadingDialog = new LoadingDialog(mainController, progressInfoText);
		} else {
			loadingDialog = null;
		}
		taskThread = new Thread(threadTask);		
		monitorThread = new MonitorThread(taskThread, loadingDialog);	
		taskThread.setName(String.format("thread-%s", taskCounter));
		monitorThread.setName(String.format("monitor-%s", taskCounter));
	}
	
	public LoadingDialog getLoadingDialog() {
		return this.loadingDialog;
	}
	
	public Thread getThread() {
		return this.taskThread;
	}

	@Override
	public void run() {
		taskThread.start();
		monitorThread.start();
		if (loadingDialog != null) {
			loadingDialog.run();
		}
	}
}
