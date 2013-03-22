package husacct.control;

import husacct.common.services.IObservableService;
import husacct.control.task.threading.ThreadWithLoader;

import javax.swing.JDialog;

public interface IControlService extends IObservableService {

    public void parseCommandLineArguments(String[] commandLineArguments);

    public void startApplication();

    public void showErrorMessage(String message);

    public void showInfoMessage(String message);

    public void centerDialog(JDialog dialog);

    public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask);

    public void setServiceListeners();
}
