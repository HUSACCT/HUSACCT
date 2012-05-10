package husacct.define.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.apache.log4j.Logger;

public abstract class PopUpController {
	public static final String ACTION_NEW = "NEW";
	public static final String ACTION_EDIT = "EDIT";
	
	protected String action = PopUpController.ACTION_NEW;
	protected long moduleId;
	protected Logger logger;
	
	protected List<Observer> observers;
	
	public PopUpController(){
		observers = new ArrayList<Observer>();
		logger = Logger.getLogger(DefinitionController.class);
	}

	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	protected long getModuleId() {
		return moduleId;
	}

	public void setAction(String action) {
		if (action.equals(PopUpController.ACTION_EDIT) || action.equals(PopUpController.ACTION_NEW)) {
			this.action = action;
		}
	}

	public String getAction() {
		return action;
	}
}
