package husacct.control;

import java.util.Locale;

public interface IControlService {
	
	public void addLocaleChangeListener(ILocaleChangeListener listener);
	public Locale getLocale();
	public void showErrorMessage(String message);
	public void showInfoMessage(String message);
	public String getTranslatedString(String stringIdentifier);

}
