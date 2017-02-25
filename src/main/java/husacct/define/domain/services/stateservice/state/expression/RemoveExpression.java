package husacct.define.domain.services.stateservice.state.expression;

import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;

public class RemoveExpression implements Istate {

	private ExpressionUnitDefinition data;
	private long moduleId;
	
	 public RemoveExpression(long moduleId,ExpressionUnitDefinition expression)
	{
		this.data=expression;
		this.moduleId=moduleId;
		
	}
	
	
	@Override
	public void undo() {
		UndoRedoService.getInstance().addExpression(moduleId, data);

	}

	@Override
	public void redo() {
		
		UndoRedoService.getInstance().removeExpression(moduleId, data);

	}

}
