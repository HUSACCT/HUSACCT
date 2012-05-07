package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.ControlServiceImpl;


import java.util.ArrayList;
import java.util.Locale;

public class LocaleController {
	
	private static Locale currentLocale = Locale.ENGLISH;
	
	public LocaleController(){
		setLocale(LocaleController.currentLocale);
	}
	
	public static Locale getLocale(){
		return LocaleController.currentLocale;
	}
	
	public void setLocale(Locale locale){
		ControlServiceImpl service = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		LocaleController.currentLocale = locale;
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
}
