package husaccttest.graphics;

import java.util.Locale;

import javax.swing.JDialog;

import husacct.common.services.IServiceListener;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.threading.ThreadWithLoader;

public class ControlServiceStub implements IControlService {

	@Override
	public void addServiceListener(IServiceListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyServiceListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startApplication() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addLocaleChangeListener(ILocaleChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showErrorMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showInfoMessage(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTranslatedString(String stringIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void centerDialog(JDialog dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setServiceListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void parseCommandLineArguments(String[] commandLineArguments) {
		// TODO Auto-generated method stub
		
	}

}
