package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class LayerUpCommand implements Istate {

	private long moduleId;

	public LayerUpCommand(long moduleId) {
		this.moduleId=moduleId;
	}
	
	@Override
	public void undo() {

		UndoRedoService.getInstance().layerDown(moduleId);
	
	}

	@Override
	public void redo() {
		
	UndoRedoService.getInstance().layerUp(moduleId);
	}

}
