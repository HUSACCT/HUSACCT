package husacct.define.task;

import husacct.define.domain.DefineDomainService;

import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

public abstract class PopUpController extends Observable implements ActionListener {
	public static final String ACTION_NEW = "NEW";
	public static final String ACTION_EDIT = "EDIT";
	public ResourceBundle resourceBundle = ResourceBundle.getBundle("husacct/define/presentation/gui", new Locale("en", "GB"));

	protected DefineDomainService defineDomainService = DefineDomainService.getInstance();
	protected String action = PopUpController.ACTION_NEW;
	protected long layer_id;

	public abstract void initUi() throws Exception;

	public abstract void save();

//	public abstract void addExceptionRow();

//	public abstract void removeExceptionRow();

	/**
	 * Use this function to notify the definitioncontroller that there is a change
	 */
	protected void pokeObservers() {
		setChanged();
		notifyObservers();
	}

	public void setLayerID(long layerId) {
		this.layer_id = layerId;
	}

	protected long getLayerID() {
		return layer_id;
	}

	public void setAction(String action) {
		if (action.equals(PopUpController.ACTION_EDIT) || action.equals(PopUpController.ACTION_NEW)) {
			this.action = action;
		}
	}

	protected String getAction() {
		return action;
	}

}
