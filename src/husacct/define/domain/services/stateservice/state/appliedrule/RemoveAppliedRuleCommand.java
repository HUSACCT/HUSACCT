package husacct.define.domain.services.stateservice.state.appliedrule;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class RemoveAppliedRuleCommand implements Istate {

	private ArrayList<AppliedRuleStrategy> appliedRulesId;
	
	
	public RemoveAppliedRuleCommand(ArrayList<AppliedRuleStrategy> rules) {
		this.appliedRulesId=rules;
	}
	
	@Override
	public void undo() {
for (AppliedRuleStrategy id : appliedRulesId) {
	SoftwareArchitecture.getInstance().addAppliedRule(id);
}

	}

	@Override
	public void redo() {
		for (AppliedRuleStrategy id : appliedRulesId) {
			SoftwareArchitecture.getInstance().removeAppliedRule(id.getId());
		}

	}

}
