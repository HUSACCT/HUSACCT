package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public class LocaleController {

	private String bundleLocation = "husacct.common.locale";
	private String bundlePrefix = "husacct";

	private ResourceBundle resourceBundle;
	private Logger logger = Logger.getLogger(LocaleController.class);

	private ArrayList<ILocaleChangeListener> listeners = new  ArrayList<ILocaleChangeListener>();

	public static Locale english = Locale.ENGLISH;
	public static Locale dutch = new Locale("nl", "NL");
	private static Locale defaultLocale = english;

	private Locale currentLocale;

	private List<Locale> availableLocales = new ArrayList<Locale>();

	public LocaleController(){
		currentLocale = defaultLocale;
		scanForLocales();
		reloadBundle();
	}

	private void scanForLocales(){
		String[] files = null;
		try {
			files = getResourceListing("husacct/common/locale/");
		} catch (Exception e) {
			logger.debug("Unable to find locales dynamically. falling back to EN and NL");
			availableLocales.add(LocaleController.english);
			availableLocales.add(LocaleController.dutch);
		}

		String subStringStart = bundlePrefix + "_";
		String subStringEnd = ".properties";

		for(String file : files){
			if(file.startsWith(subStringStart) && file.endsWith(subStringEnd)){
				String locale = file.substring(subStringStart.length(), file.indexOf("."));
				availableLocales.add(new Locale(locale, locale));
			}
		}
	}

	public void setLocale(Locale locale){
		currentLocale = locale;
		reloadBundle();
		notifyLocaleListeners();
	}

	public List<Locale> getAvailableLocales(){
		return availableLocales;
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
			resourceBundle = ResourceBundle.getBundle(bundleLocation + "." + bundlePrefix, getLocale());
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

	/**
	 * List directory contents for a resource folder. Not recursive.
	 * This is basically a brute-force implementation.
	 * Works for regular files and also JARs.
	 * 
	 * @author Greg Briggs, http://www.uofr.net/~greg/java/get-resource-listing.html
	 * @param clazz Any java class that lives in the same place as the resources you want.
	 * @param path Should end with "/", but not start with one.
	 * @return Just the name of each member item, not the full paths.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	private String[] getResourceListing(String path) throws URISyntaxException, IOException {
		URL dirURL = getClass().getClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list();
		} 

		if (dirURL == null) {
			/* 
			 * In case of a jar file, we can't actually find a directory.
			 * Have to assume the same jar as clazz.
			 */
			String me = getClass().getName().replace(".", "/")+".class";
			dirURL = getClass().getClassLoader().getResource(me);
		}

		if (dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) { //filter according to the path
					String entry = name.substring(path.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			return result.toArray(new String[result.size()]);
		} 

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
}
