package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class AppliedRuleCommand  implements Istate{
    private AppliedRuleStrategy data;
	public AppliedRuleCommand(AppliedRuleStrategy rule) {
	this.data=rule;
	}
	
	@Override
	public void undo() {
		SoftwareArchitecture.getInstance().removeAppliedRule(data.getId());
		
	}

	@Override
	public void redo() {
	 SoftwareArchitecture.getInstance().addAppliedRule(data);
		
	}
}
