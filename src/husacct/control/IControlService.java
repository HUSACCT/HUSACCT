package husacct.control;

import java.util.Locale;

public interface IControlService {
	
	public void addLocaleChangeListener(ILocaleChangeListener listener);
	public Locale getLocale();
	
}
