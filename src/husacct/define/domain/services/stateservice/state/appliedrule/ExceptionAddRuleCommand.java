package husacct.define.domain.services.stateservice.state.appliedrule;

import java.util.ArrayList;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class ExceptionAddRuleCommand implements Istate {

	private ArrayList<AppliedRuleStrategy> data;
	private AppliedRuleStrategy parent;
	public ExceptionAddRuleCommand(AppliedRuleStrategy parent,ArrayList<AppliedRuleStrategy> rules) {
		this.data=rules;
		this.parent=parent;
	}
	
	
	@Override
	public void undo() {
     for (AppliedRuleStrategy rule : data) {
		parent.removeException(rule);
	}

	}

	@Override
	public void redo() {
		   for (AppliedRuleStrategy rule : data) {
				parent.addException(rule);
			}

	}

}
