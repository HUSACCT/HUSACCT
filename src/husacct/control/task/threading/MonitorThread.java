package husacct.control.task.threading;

import husacct.control.presentation.util.LoadingDialog;

import org.apache.log4j.Logger;

public class MonitorThread extends Thread implements Runnable {

    private Logger logger = Logger.getLogger(MonitorThread.class);
    private Thread taskThread;
    private LoadingDialog loadingDialog;

    public MonitorThread(Thread taskThread, LoadingDialog loadingDialog) {
        this.taskThread = taskThread;
        this.loadingDialog = loadingDialog;
    }

    @Override
    public void run() {
        try {
            taskThread.join();
            loadingDialog.dispose();
            logger.debug(String.format("thread [%s] finished, removed loader", taskThread.getName()));
        } catch (InterruptedException exception) {
            logger.debug(String.format("thread [%s] interupted", taskThread.getName()));
        }

    }
}
