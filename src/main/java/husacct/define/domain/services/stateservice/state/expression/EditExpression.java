package husacct.define.domain.services.stateservice.state.expression;

import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;

public class EditExpression implements Istate {
   private ExpressionUnitDefinition oldExpression;
   private ExpressionUnitDefinition newExpression;
   private long moduleId;
	public EditExpression( long moduleId, ExpressionUnitDefinition[] oldAndnew) {
	this.moduleId=moduleId;
	this.oldExpression= oldAndnew[0];
	this.newExpression= oldAndnew[1];
	}
	
	@Override
	public void undo() {
		UndoRedoService.getInstance().editExpression(moduleId, newExpression, oldExpression);
	}

	@Override
	public void redo() {
		UndoRedoService.getInstance().editExpression(moduleId,oldExpression,newExpression);

	}

}
