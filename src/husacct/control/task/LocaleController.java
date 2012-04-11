package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.ControlServiceImpl;


import java.util.ArrayList;
import java.util.Locale;

public class LocaleController {
	
	private Locale currentLocale;
	
	public LocaleController(){
		setLocale(Locale.ENGLISH);
	}
	
	public Locale getLocale(){
		return this.currentLocale;
	}
	
	public void setLocale(Locale locale){
		ControlServiceImpl service = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		this.currentLocale = locale;
		service.notifyLocaleListeners(locale);
	}
	
	public ArrayList<Locale> getAvailableLocales(){
		ArrayList<Locale> locales = new ArrayList<Locale>();
		
		locales.add(Locale.ENGLISH);
		locales.add(Locale.GERMAN);
		
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
}
