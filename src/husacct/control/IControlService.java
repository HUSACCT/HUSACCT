package husacct.control;

import husacct.common.services.IObservableService;

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
	public List<String> getStringIdentifiers(String translatedString);
	public void centerDialog(JDialog dialog);

}
