package husacct.control.task.configuration;

import husacct.common.OSDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigurationManager {

	private final static Properties properties = loadProperties();
	private final static ArrayList<IConfigListener> listeners = new ArrayList<IConfigListener>();
	
	public static String getProperty(String key) {
		if(properties.containsKey(key))
			return properties.getProperty(key);
		else
			return "";
	}

	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public static void setPropertyIfEmpty(String key, String value) {
		if(!properties.containsKey(key) || properties.getProperty(key).equals(""))
			properties.setProperty(key, value);
	}
	
	private static Properties loadProperties() {
		Properties props = new Properties();
		try {
			File directory = new File(OSDetector.getAppFolder());
			File file = new File(OSDetector.getAppFolder() + File.separator + "config.properties");
			if(!directory.exists()) {
				directory.mkdir();
				file.createNewFile();
			}	
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	public static void storeProperties() {
		storeProperties(properties);
	}
	
	public static void storeProperties(Properties props) {
		try {
			props.store(new FileOutputStream(OSDetector.getAppFolder() + File.separator + "config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void notifyListeners() {
		for(IConfigListener event : listeners)
			event.onConfigUpdate();
	}
	
	public static void addListener(IConfigListener event) {
		listeners.add(event);
	}
}