package husacct.validate.presentation;

import husacct.ServiceProvider;

public class DataLanguageHelper {
	
	public String key;
	
	public DataLanguageHelper(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return ServiceProvider.getInstance().getControlService().getTranslatedString(key);
	}
}