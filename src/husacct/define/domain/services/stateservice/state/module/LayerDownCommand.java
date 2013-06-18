package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;


public class LayerDownCommand  implements Istate{

	private long moduleId;

	public LayerDownCommand(long moduleId) {
		this.moduleId=moduleId;
	}
	
	@Override
	public void undo() {

		UndoRedoService.getInstance().layerUp(moduleId);
		
	}

	@Override
	public void redo() {
	
		UndoRedoService.getInstance().layerDown(moduleId);
		
		
	}

}
