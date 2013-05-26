package husacct.define.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

public abstract class PopUpController extends Observable {
    public static final String ACTION_EDIT = "EDIT";
    public static final String ACTION_NEW = "NEW";

    protected String action = PopUpController.ACTION_NEW;
    protected long currentModuleId;
    protected Logger logger;

    protected List<Observer> observers;

    public PopUpController() {
	observers = new ArrayList<Observer>();
	logger = Logger.getLogger(DefinitionController.class);
    }

    @Override
    public void addObserver(Observer o) {
	if (!observers.contains(o)) {
	    observers.add(o);
	}
    }

    public String getAction() {
	return action;
    }

    protected long getModuleId() {
	return currentModuleId;
    }

    @Override
    public void notifyObservers() {
	for (Observer o : observers) {
	    o.update(this, o);
	}
    }

    public void removeObserver(Observer o) {
	if (observers.contains(o)) {
	    observers.remove(o);
	}
    }

    public void setAction(String action) {
	if (action.equals(PopUpController.ACTION_EDIT)
		|| action.equals(PopUpController.ACTION_NEW)) {
	    this.action = action;
	}
    }

    public void setModuleId(long moduleId) {
	currentModuleId = moduleId;
    }
}
