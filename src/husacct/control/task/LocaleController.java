package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class LocaleController {
	
	private String bundleLocation = "husacct.common.locale.husacct";
	private ResourceBundle resourceBundle;
	private Logger logger = Logger.getLogger(LocaleController.class);
	
	private ArrayList<ILocaleChangeListener> listeners = new  ArrayList<ILocaleChangeListener>();
	
	public static Locale english = Locale.ENGLISH;
	public static Locale nederlands = new Locale("nl", "NL");
	private static Locale defaultLocale = english;
	
	private Locale currentLocale;
	
	public LocaleController(){
		currentLocale = defaultLocale;
		reloadBundle();
	}
	
	public void setLocale(Locale locale){
		currentLocale = locale;
		reloadBundle();
		notifyLocaleListeners();
	}
	
	public ArrayList<Locale> getAvailableLocales(){
		ArrayList<Locale> locales = new ArrayList<Locale>();
		
		locales.add(english);
		locales.add(nederlands);
		
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
	
	private void reloadBundle(){
		try {
			resourceBundle = ResourceBundle.getBundle(bundleLocation, getLocale());
		} catch (Exception e){
			logger.debug("Unable to reload resource bundle: " + e.getMessage());
		}
	}
	
	public Locale getLocale(){
		return currentLocale;
	}

	public String getTranslatedString(String key){
		try {
			key = resourceBundle.getString(key);
		} catch (MissingResourceException missingResourceException){
			logger.debug(String.format("Unable to find translation for key %s in %s_%s.properties", key, bundleLocation, resourceBundle.getLocale().getLanguage()));
		}
		return key;
	}
	
	public List<String> getStringIdentifiers(String translatedString){
		Enumeration<String> keys = resourceBundle.getKeys();
		
		List<String> matches = new ArrayList<String>();
		
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(translatedString.equals(getTranslatedString(key))){
				matches.add(key);
			}
		}
		
		return matches;
	}
	
	public void addLocaleChangeListener(ILocaleChangeListener listener){
		listeners.add(listener);
	}
	
	private void notifyLocaleListeners(){		
		// Copy the current listeners to avoid ConcurrentModificationExceptions
		// Usually triggered when a listener is added while notifying the listeners
		@SuppressWarnings("unchecked")
		ArrayList<ILocaleChangeListener> listenersCopy = (ArrayList <ILocaleChangeListener>) this.listeners.clone();
		
		for(ILocaleChangeListener listener : listenersCopy){
			logger.debug("Notifying: " + listener.getClass());
			listener.update(currentLocale);
		}
		
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		controlService.notifyServiceListeners();
		
	}
}
