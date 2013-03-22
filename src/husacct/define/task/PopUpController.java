package husacct.define.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

public abstract class PopUpController extends Observable {

    public static final String ACTION_NEW = "NEW";
    public static final String ACTION_EDIT = "EDIT";
    protected String action = PopUpController.ACTION_NEW;
    protected long currentModuleId;
    protected Logger logger;
    protected List<Observer> observers;

    public void addObserver(Observer o) {
        if (!this.observers.contains(o)) {
            this.observers.add(o);
        }
    }

    public void removeObserver(Observer o) {
        if (this.observers.contains(o)) {
            this.observers.remove(o);
        }
    }

    public void notifyObservers() {
        for (Observer o : this.observers) {
            o.update(this, o);
        }
    }

    public PopUpController() {
        observers = new ArrayList<Observer>();
        logger = Logger.getLogger(DefinitionController.class);
    }

    public void setModuleId(long moduleId) {
        this.currentModuleId = moduleId;
    }

    protected long getModuleId() {
        return currentModuleId;
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
