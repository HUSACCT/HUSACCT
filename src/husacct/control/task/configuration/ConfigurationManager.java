package husacct.control.task.configuration;

import java.util.HashMap;

public class ConfigurationManager {

	HashMap<String, String> settings = new HashMap<String, String>();
	
	
	public ConfigurationManager() {
		
	}
	
	
	public String getSetting(String key) throws NonExistingSettingException {
		if(settings.containsKey(key))
			return settings.get(key);
		throw new NonExistingSettingException("The setting " + key + " does not exist.");
	}
	
	public void setSetting(String key, String value) {
		settings.put(key, value);
	}
	
	public void loadSettings() {
		//TODO Read out File and set settings.
	}
	
	public void createDefaults() {
		//TODO Create default settings file.
	}
}
