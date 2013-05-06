package husacct.control.task.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
			file.createNewFile();
			File defaults = new File("/husacct/common/resources/config.properties");
			FileInputStream defaultStream = new FileInputStream(defaults);
			FileOutputStream destinationStream = new FileOutputStream(file);
			FileChannel source = defaultStream.getChannel();
			FileChannel destination = destinationStream.getChannel();
			destination.transferFrom(source, 0, source.size());
			defaultStream.close();
			destinationStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}