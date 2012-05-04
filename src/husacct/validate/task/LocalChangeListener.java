package husacct.validate.task;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.validate.IValidateService;

import java.util.Locale;

public class LocalChangeListener implements ILocaleChangeListener{

	@Override
	public void update(Locale newLocale) {
		ServiceProvider sp = ServiceProvider.getInstance();
		IValidateService vs = sp.getValidateService();
		vs.reloadGUI();
	}
	
}
