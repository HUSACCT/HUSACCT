package husacct.define.domain.services.stateservice.state.appliedrule;

import java.util.ArrayList;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class AppliedRuleAddCommand  implements Istate{
    private ArrayList<AppliedRuleStrategy>  data;
	public AppliedRuleAddCommand(ArrayList<AppliedRuleStrategy> rules) {
	this.data=rules;
	}
	
	@Override
	public void undo() {
		for ( AppliedRuleStrategy rule : data) {
			SoftwareArchitecture.getInstance().removeAppliedRule(rule.getId());
		}
		
		
	}

	@Override
	public void redo() {
		for ( AppliedRuleStrategy rule : data) {
		SoftwareArchitecture.getInstance().addAppliedRule(rule);
		}
	}
}
