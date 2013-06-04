package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;

public class LayerDownCommand  implements Istate{

	private long moduleId;

	public LayerDownCommand(long moduleId) {
		this.moduleId=moduleId;
	}
	
	@Override
	public void undo() {
		DefinitionController.getInstance().moveLayerUp(moduleId);
		
	}

	@Override
	public void redo() {
		DefinitionController.getInstance().moveLayerDown(moduleId);
		
	}

}
