package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.ControlServiceImpl;

import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class LocaleController {
	
	private static Locale currentLocale = Locale.ENGLISH;
	private static final String bundleLocation = "husacct.common.locale.husacct";
	private static ResourceBundle resourceBundle;
	private static Logger logger = Logger.getLogger(LocaleController.class);
	
	public LocaleController(){
		setLocale(LocaleController.currentLocale);
	}
	
	public void setLocale(Locale locale){
		ControlServiceImpl service = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		LocaleController.currentLocale = locale;
		reloadBundle();
		service.notifyLocaleListeners(locale);
	}
	
	public ArrayList<Locale> getAvailableLocales(){
		ArrayList<Locale> locales = new ArrayList<Locale>();
		
		locales.add(Locale.ENGLISH);
		locales.add(new Locale("nl", "NL"));
		
		return locales;
	}

	public void setNewLocaleFromString(String language) {
		
		for(Locale locale : getAvailableLocales()){
			if(language.equals(locale.getLanguage())){
				setLocale(locale);
				break;
			}
		}

	}
	
	public void reloadBundle(){
		LocaleController.resourceBundle = ResourceBundle.getBundle(bundleLocation, getLocale());
	}
	
	public static Locale getLocale(){
		return LocaleController.currentLocale;
	}

	public static String getTranslatedString(String key){
		try {
			key = LocaleController.resourceBundle.getString(key);
		} catch (MissingResourceException missingResourceException){
			logger.debug(missingResourceException.getMessage());
		}
		return key;
	}
}
