package husacct.common.locale;

import husacct.common.services.IObservableService;

import java.util.List;
import java.util.Locale;

public interface ILocaleService extends IObservableService{

	public Locale getLocale();
	public void setLocale(Locale newLocale);
	public String getTranslatedString(String stringIdentifier);
	public List<Locale> getAvailableLocales();
	
}