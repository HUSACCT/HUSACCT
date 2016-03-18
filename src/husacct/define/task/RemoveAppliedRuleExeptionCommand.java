package husacct.define.task;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class RemoveAppliedRuleExeptionCommand implements Istate {

	
	public RemoveAppliedRuleExeptionCommand(long parentRuleId, AppliedRuleStrategy exceptionRule) {
		// TODO Auto-generated constructor stub
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
