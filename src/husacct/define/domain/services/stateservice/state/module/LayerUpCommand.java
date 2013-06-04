package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;

public class LayerUpCommand implements Istate {

	private long moduleId;

	public LayerUpCommand(long moduleId) {
		this.moduleId=moduleId;
	}
	
	@Override
	public void undo() {
		DefinitionController.getInstance().moveLayerDown(moduleId);

	}

	@Override
	public void redo() {
		DefinitionController.getInstance().moveLayerUp(moduleId);

	}

}
