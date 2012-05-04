package husacct.define.task;

import husacct.define.domain.DefineDomainService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public abstract class PopUpController extends Observable {
	public static final String ACTION_NEW = "NEW";
	public static final String ACTION_EDIT = "EDIT";
	public ResourceBundle resourceBundle = ResourceBundle.getBundle("husacct/common/locale/defineLang", new Locale("en", "GB"));

	protected DefineDomainService defineDomainService = DefineDomainService.getInstance();
	protected String action = PopUpController.ACTION_NEW;
	protected long moduleId;
	
	protected List<Observer> observers;
	
	public PopUpController(){
		observers = new ArrayList<Observer>();
	}

	public abstract void initUi() throws Exception;
	
	/**
	 * Use this function to notify the definitioncontroller that there is a change
	 */
	protected void pokeObservers() {
		setChanged();
		notifyObservers();
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

	protected String getAction() {
		return action;
	}

	public void addObserver(Observer o){
		if (!this.observers.contains(o)){
			this.observers.add(o);
		}
	}
	
	public void removeObserver(Observer o){
		if (this.observers.contains(o)){
			this.observers.remove(o);
		}
	}
	
	@Override
	public void notifyObservers(){
		for (Observer o : this.observers){
			o.update(this, moduleId);
		}
	}
}
