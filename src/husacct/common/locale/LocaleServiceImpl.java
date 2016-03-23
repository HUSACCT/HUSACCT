package husacct.common.locale;

import husacct.common.Resource;
import husacct.common.services.ObservableService;
import husacct.control.task.configuration.ConfigurationManager;

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

public class LocaleServiceImpl extends ObservableService implements ILocaleService{
	
	private Logger logger = Logger.getLogger(LocaleServiceImpl.class);
	
	public static Locale english = Locale.ENGLISH;
	public static Locale dutch = new Locale("nl", "NL");
	
	private String bundleLocation = Resource.LOCALE_PATH;
	private String bundlePrefix = "husacct";
	private String bundleSuffix = ".properties";
	private Locale defaultLocale;
	
	private ResourceBundle resourceBundle;
	private Locale currentLocale;

	private List<Locale> availableLocales = new ArrayList<Locale>();

	public LocaleServiceImpl(){
		detectLocales();
		String locale = ConfigurationManager.getProperty("Language");
		defaultLocale = new Locale(locale, locale);
		setLocale(defaultLocale);
	}

	private void detectLocales(){
		String[] fileNames = null;
		try {
			fileNames = getResourceListing(bundleLocation);
			
			for(String fileName : fileNames){
				if(fileName.startsWith(bundlePrefix + "_") && fileName.endsWith(bundleSuffix)){
					String locale = fileName.substring(bundlePrefix.length()+1, fileName.indexOf("."));
					Locale detectedLocale = new Locale(locale, locale);
					availableLocales.add(detectedLocale);
				}
			}
		} catch (Exception e) {
			logger.debug("Unable to find locales dynamically.");
			availableLocales.add(defaultLocale);
		}
	}

	@Override
	public void setLocale(Locale locale){
		if(isAvailableLocale(locale)){
			currentLocale = locale;
			loadBundle();
			notifyServiceListeners();
		} else {
			logger.warn("Trying to set non-existing locale " + locale.getLanguage());
		}
	}
	
	private void loadBundle(){
		try {
			String path = bundleLocation.replace('/', '.').substring(1) + bundlePrefix;
			resourceBundle = ResourceBundle.getBundle(path, getLocale());
		} catch (Exception e){
			logger.debug("Unable to reload resource bundle: " + e.getMessage());
		}
	}
	
	@Override
	public List<Locale> getAvailableLocales(){
		return availableLocales;
	}
	
	private boolean isAvailableLocale(Locale locale){
		String language = locale.getLanguage();
		for(Locale availableLocale : getAvailableLocales()){
			String availableLanguage = availableLocale.getLanguage();
			if(language.equals(availableLanguage)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Locale getLocale(){
		return currentLocale;
	}

	@Override
	public String getTranslatedString(String key){
		String transKey = "";
		if((key == null) || (key == ""))
			return "";
		try {
			transKey = resourceBundle.getString(key);
		} catch (MissingResourceException missingResourceException){
			return key + " (Missing resource in Locale service)"; 
			//logger.debug(String.format("Unable to find translation for key %s in %s_%s.properties" + key + resourceBundle.getLocale().getLanguage()));
			//missingResourceException.printStackTrace();
		}
		return transKey;
	}
	
	/**
	 * List directory contents for a resource folder. Not recursive.
	 * This is basically a brute-force implementation.
	 * Works for regular files and also JARs.
	 * 
	 * @author Greg Briggs, http://www.uofr.net/~greg/java/get-resource-listing.html
	 * @param clazz Any java class that lives in the same place as the resources you want.
	 * @param strippedPath Should end with "/", but not start with one.
	 * @return Just the name of each member item, not the full paths.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	private String[] getResourceListing(String path) throws URISyntaxException, IOException {
		String strippedPath = path.substring(1);
		URL dirURL = getClass().getClassLoader().getResource(strippedPath);
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
				if (name.startsWith(strippedPath)) { //filter according to the path
					String entry = name.substring(strippedPath.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			jar.close();
			return result.toArray(new String[result.size()]);
		} 

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}
}
