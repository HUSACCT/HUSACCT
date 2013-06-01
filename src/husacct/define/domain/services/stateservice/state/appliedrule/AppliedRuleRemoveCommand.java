package husacct.define.domain.services.stateservice.state.appliedrule;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class AppliedRuleRemoveCommand implements Istate {

	 private AppliedRuleStrategy data;
		public AppliedRuleRemoveCommand(AppliedRuleStrategy rule) {
		this.data=rule;
		}
	
	@Override
	public void undo() {
		SoftwareArchitecture.getInstance().addAppliedRule(data);

	}

	@Override
	public void redo() {
		SoftwareArchitecture.getInstance().removeAppliedRule(data.getId());

	}

}
