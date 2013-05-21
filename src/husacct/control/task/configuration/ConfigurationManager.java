package husacct.control.task.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

	private final static Properties properties = loadProperties();
	
	public static String getProperty(String key, String defaultParam) {
		if(properties.containsKey(key))
			return properties.getProperty(key);
		else {
			setProperty(key, defaultParam);
			return defaultParam;
		}
	}
	
	public static int getPropertyAsInteger(String key, String defaultParam) throws NumberFormatException {
		String property = getProperty(key, defaultParam);
		return Integer.parseInt(property);
	}
	
	public static boolean getPropertyAsBoolean(String key, String defaultParam) {
		String property = getProperty(key, defaultParam);
		return Boolean.parseBoolean(property);
	}
	
	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public static void setPropertyFromInteger(String key, int value) {
		properties.setProperty(key, String.valueOf(value));
	}
	
	public static void setPropertyFromBoolean(String key, boolean value) {
		properties.setProperty(key, String.valueOf(value));
	}
	
	public static void setPropertie(String key, String value) {
		properties.setProperty(key, value);
		storeProperties();
	}
	
	private static Properties loadProperties() {
		Properties props = new Properties();
		try {
			File file = new File("config.properties");
			if(!file.isFile())
				createDefaults(file);
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	public static void storeProperties() {
		try {
			properties.store(new FileOutputStream("config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createDefaults(File file) {
		
		try {
			properties.load(ConfigurationManager.class.getResourceAsStream("/husacct/common/resources/config.properties"));
			storeProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}