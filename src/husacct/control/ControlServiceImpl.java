package husacct.control;

import husacct.common.savechain.ISaveable;
import husacct.control.task.LocaleController;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.jdom2.Element;


public class ControlServiceImpl implements IControlService, ISaveable{

	private Logger logger = Logger.getLogger(ControlServiceImpl.class);
	ArrayList<ILocaleChangeListener> listeners = new ArrayList<ILocaleChangeListener>();

	public void addLocaleChangeListener(ILocaleChangeListener listener) {
		this.listeners.add(listener);
	}

	public Locale getLocale() {
		return LocaleController.getLocale();
	}
	
	public void notifyLocaleListeners(Locale newLocale){
		for(ILocaleChangeListener listener : this.listeners){
			listener.update(newLocale);
		}
	}

	public Element getWorkspaceData() {
		Element data = new Element("MyControlData");
		data.addContent("testdata");
		return data;
	}

	public void loadWorkspaceData(Element workspaceData) {
		logger.debug(workspaceData);		
	}

}
