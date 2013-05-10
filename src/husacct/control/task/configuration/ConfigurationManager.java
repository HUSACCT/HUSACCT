package husacct.control.task.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {

	Properties properties = new Properties();
	
	
	public ConfigurationManager() {
		loadProperties();
	}
	
	
	public String getProperty(String key) throws NonExistingSettingException {
		if(properties.containsKey(key))
			return properties.getProperty(key);
		throw new NonExistingSettingException("The setting " + key + " does not exist.");
	}
	
	public int getPropertyAsInteger(String key) throws NonExistingSettingException, NumberFormatException {
		String property = getProperty(key);
		return Integer.parseInt(property);
	}
	
	public boolean getPropertyAsBoolean(String key) throws NonExistingSettingException {
		String property = getProperty(key);
		return Boolean.parseBoolean(property);
	}
	
	
	public void setPropertie(String key, String value) {
		properties.setProperty(key, value);
		storeProperties();
	}
	
	private void loadProperties() {
		try {
			File file = new File("config.properties");
			if(!file.isFile())
				createDefaults(file);
			properties.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void storeProperties() {
		try {
			properties.store(new FileOutputStream("config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createDefaults(File file) {
		
		try {
			properties.load(this.getClass().getResourceAsStream("/husacct/common/resources/config.properties"));
			storeProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}