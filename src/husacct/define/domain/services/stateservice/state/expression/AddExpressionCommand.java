package husacct.define.domain.services.stateservice.state.expression;

import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;

public class AddExpressionCommand implements Istate {
  
	private ExpressionUnitDefinition data;
	private long moduleId;
	
	
	public AddExpressionCommand(long moduleId,ExpressionUnitDefinition expression)
	{
		this.data=expression;
		this.moduleId=moduleId;
		
	}
	
	
	@Override
	public void undo() {
		UndoRedoService.getInstance().removeExpression(moduleId, data);
		
	}

	@Override
	public void redo() {
		UndoRedoService.getInstance().addExpression(moduleId, data);
		UndoRedoService.getInstance().removeExpression(moduleId, data);
	}

}
