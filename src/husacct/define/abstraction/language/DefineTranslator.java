package husacct.define.abstraction.language;

import husacct.ServiceProvider;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class DefineTranslator {
	private static final String bundleLocation = "husacct.common.locale.defineLang";
	private static Logger logger = Logger.getLogger(DefineTranslator.class);

	public static String translate(String key){	
		String value = key;
		
		try{
			Locale locale = getLocale();
			ResourceBundle languageValues = ResourceBundle.getBundle(bundleLocation, locale);
			value = languageValues.getString(key);
		}catch(MissingResourceException m){
			logger.warn(m.getMessage(), m);
		}
		return value;
	}

	public static String getKey(String remoteValue){
		String key = remoteValue;
		try{	
			Locale locale = getLocale();
			ResourceBundle languageValues = ResourceBundle.getBundle(bundleLocation, locale);
			Enumeration<String> keyList = languageValues.getKeys();
			while (keyList.hasMoreElements()) {
				String tmpKey = (String)keyList.nextElement();
				String value = languageValues.getString(tmpKey);
				if(value.equals(remoteValue)){
					key = tmpKey;
				}
			}
		}catch(MissingResourceException m){
			logger.error(m.getMessage(), m);
		}
		return key;
	}

	private static Locale getLocale() {
		return ServiceProvider.getInstance().getControlService().getLocale();
	}
}