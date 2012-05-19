package husacct.control;

import java.util.List;
import java.util.Locale;

public interface IControlService {

	public void startApplication();
	public void startApplication(String[] userArguments);
	public void addLocaleChangeListener(ILocaleChangeListener listener);
	public Locale getLocale();
	public void showErrorMessage(String message);
	public void showInfoMessage(String message);
	public String getTranslatedString(String stringIdentifier);
	public List<String> getStringIdentifiers(String translatedString);

}
