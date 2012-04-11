package husacct.validate.abstraction.language;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundles {
	private static final String bundleLocation = "husacct.common.locale.validateLang";

	public static String getValue(String key){
		try{
			ResourceBundle languageValues = ResourceBundle.getBundle(bundleLocation, Locale.ENGLISH);
			return languageValues.getString(key);
		}catch(MissingResourceException m){
			System.out.println(m);
		}
		return "";
	}
}
