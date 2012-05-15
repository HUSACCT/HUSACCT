package husacct.control;

import husacct.common.savechain.ISaveable;
import husacct.control.domain.Workspace;
import husacct.control.task.ApplicationController;
import husacct.control.task.LocaleController;
import husacct.control.task.WorkspaceController;

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
		Element data = new Element("workspace");
		Workspace workspace = WorkspaceController.getCurrentWorkspace();
		data.setAttribute("name", workspace.getName());
		return data;
	}

	public void loadWorkspaceData(Element workspaceData) {
		try {
			String workspaceName = workspaceData.getAttributeValue("name");
			Workspace workspace = new Workspace();
			workspace.setName(workspaceName);
			WorkspaceController.setWorkspace(workspace);			
		} catch (Exception e){
			logger.debug("WorkspaceData corrupt: " + e);
		}
	}
	
	public void showErrorMessage(String message){
		ApplicationController.showErrorMessage(message);
	}
	
	public String getTranslatedString(String stringIdentifier){
		return LocaleController.getTranslatedString(stringIdentifier);
	}

}
