package husacct.define.domain.services.stateservice.state.appliedrule;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class RemoveAppliedRuleCommand implements Istate {

	private ArrayList<AppliedRuleStrategy> appliedRules;

	
	
	public RemoveAppliedRuleCommand(ArrayList<AppliedRuleStrategy> rules) {
		this.appliedRules=rules;
	}
	
	@Override
	public void undo() {
		UndoRedoService.getInstance().addSeperatedAppliedRule(appliedRules);
		

	


	}

	@Override
	public void redo() {
		UndoRedoService.getInstance().removeSeperatedAppliedRule(appliedRules);
		

}
}