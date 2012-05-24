package husacct.control;

import husacct.common.services.IObservableService;
import husacct.control.task.threading.ThreadWithLoader;

import java.util.List;
import java.util.Locale;

import javax.swing.JDialog;

public interface IControlService extends IObservableService{

	public void startApplication();
	public void startApplication(String[] userArguments);
	public void addLocaleChangeListener(ILocaleChangeListener listener);
	public Locale getLocale();
	public void showErrorMessage(String message);
	public void showInfoMessage(String message);
	public String getTranslatedString(String stringIdentifier);
	/**
	 * @deprecated: As of 23 may, this method is deprecated. There is no alternative, make sure the logic
	 * does not require this. To ensure that this method is not used, it always returns an empty list.
	 */
	@Deprecated public List<String> getStringIdentifiers(String translatedString);
	public void centerDialog(JDialog dialog);
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask);

}
