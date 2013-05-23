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
	
	public static boolean isEmptyProperty(String key){
		return properties.getProperty(key).equals("");
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
			if(!file.isFile()) {
				props.load(ConfigurationManager.class.getResourceAsStream("/husacct/common/resources/config.properties"));
				props.store(new FileOutputStream("config.properties"), null);
			}
			props.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		props = performInitMutations(props);
		
		return props;
	}
	
	public static void storeProperties() {
		try {
			properties.store(new FileOutputStream("config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties performInitMutations(Properties props){
		//Always overwrite platform independent AppDataFolder (always empty on startup of HUSACCT)
		String appDataFolderString = System.getProperty("user.home") + File.separator + "HUSACCT" + File.separator;
		File appDataFolderObject = new File(appDataFolderString);
		if(!appDataFolderObject.exists()){
			appDataFolderObject.mkdir();
		}
		props.setProperty("PlatformIndependentAppDataFolder", appDataFolderString);
		
		if(props.getProperty("LastUsedLoadXMLWorkspacePath").equals("")){
			props.setProperty("LastUsedLoadXMLWorkspacePath", appDataFolderString + "husacct_workspace.xml");
		}
		
		if(props.getProperty("LastUsedSaveXMLWorkspacePath").equals("")){
			props.setProperty("LastUsedSaveXMLWorkspacePath", appDataFolderString + "husacct_workspace.xml");
		}
		
		//TODO: Fix this storeProperties code, because class attribute properties is still empty
		try {
			props.store(new FileOutputStream("config.properties"), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return props;
	}
}