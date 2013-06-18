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
	private final static ArrayList<IConfigEvent> listeners = new ArrayList<IConfigEvent>();
	
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
			File file = new File(OSDetector.getAppFolder() + File.separator + "config.properties");
			if(!file.isFile())
				file.createNewFile();
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
			File path = new File(OSDetector.getAppFolder());
			if(!path.isDirectory())
				path.mkdir();
			props.store(new FileOutputStream(OSDetector.getAppFolder() + File.separator + "config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyListeners() {
		for(IConfigEvent event : listeners)
			event.onConfigUpdate();
	}
	
	public void addListener(IConfigEvent event) {
		listeners.add(event);
	}
}