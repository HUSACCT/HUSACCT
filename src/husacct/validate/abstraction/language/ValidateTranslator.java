//package husacct.validate.abstraction.language;
//
//import husacct.ServiceProvider;
//
//import java.util.Enumeration;
//import java.util.Locale;
//import java.util.MissingResourceException;
//import java.util.ResourceBundle;
//
//import org.apache.log4j.Logger;
//
//public class ValidateTranslator {
//	private static final String bundleLocation = "husacct.common.locale.validateLang";
//	private static Logger logger = Logger.getLogger(ValidateTranslator.class);
//
//	public static String getValue(String key){	
//		try{
//			Locale locale = getLocale();
//			ResourceBundle languageValues = ResourceBundle.getBundle(bundleLocation, locale);
//			return languageValues.getString(key);
//		}catch(MissingResourceException m){
//			logger.warn(m.getMessage());
//			return key;
//		}
//	}
//
//	public static String getKey(String remoteValue){
//		try{	
//			Locale locale = getLocale();
//			ResourceBundle languageValues = ResourceBundle.getBundle(bundleLocation, locale);
//			Enumeration<String> keys = languageValues.getKeys();
//			while (keys.hasMoreElements()) {
//				String key = (String)keys.nextElement();
//				String value = languageValues.getString(key);
//				if(value.equals(remoteValue)){
//					return key;
//				}
//			}
//		}catch(MissingResourceException m){
//			logger.error(m.getMessage());
//			return remoteValue;
//		}
//		return remoteValue;		
//	}
//
//	private static Locale getLocale() {
//		return ServiceProvider.getInstance().getControlService().getLocale();
//	}
//}