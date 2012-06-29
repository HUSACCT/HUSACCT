package husacct.control;

import husacct.common.services.IObservableService;
import husacct.control.task.threading.ThreadWithLoader;

import java.util.Locale;

import javax.swing.JDialog;

public interface IControlService extends IObservableService{

	public void parseCommandLineArguments(String[] commandLineArguments);
	public void startApplication();
	public void addLocaleChangeListener(ILocaleChangeListener listener);
	public Locale getLocale();
	public void showErrorMessage(String message);
	public void showInfoMessage(String message);
	public String getTranslatedString(String stringIdentifier);
	public void centerDialog(JDialog dialog);
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask);
	public void setServiceListeners();

}
