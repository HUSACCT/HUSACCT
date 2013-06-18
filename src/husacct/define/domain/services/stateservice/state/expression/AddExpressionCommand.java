package husacct.define.domain.services.stateservice.state.expression;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;

public class AddExpressionCommand implements Istate {
  
	ExpressionUnitDefinition data;
	long moduleId;
	
	
	public AddExpressionCommand(long moduleId,ExpressionUnitDefinition expression)
	{
		
		
	}
	
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

}
