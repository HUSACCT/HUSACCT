package husacct.define.domain.services.stateservice.state.appliedrule;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class AppliedRuleAddCommand  implements Istate{
    private ArrayList<AppliedRuleStrategy>  data;
	public AppliedRuleAddCommand(ArrayList<AppliedRuleStrategy> rules) {
	this.data=rules;
	}
	
	@Override
	public void undo() {
		UndoRedoService.getInstance().removeSeperatedAppliedRule(data);
		
		
	}

	@Override
	public void redo() {
		UndoRedoService.getInstance().addSeperatedAppliedRule(data);
	
	
	}
}
