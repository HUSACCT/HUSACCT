package husacct.define.domain.services.stateservice.state.appliedrule;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class ExceptionRemoveRuleCommand implements Istate {

	private ArrayList<AppliedRuleStrategy> data;
	private AppliedRuleStrategy parent;
	public ExceptionRemoveRuleCommand(AppliedRuleStrategy parent,ArrayList<AppliedRuleStrategy> rules) {
		this.data=rules;
		this.parent=parent;
	}
	
	
	@Override
	public void undo() {
	UndoRedoService.getInstance().addSeperatedExeptionRule(parent.getId(),
				data);


	}

	@Override
	public void redo() {
		
		UndoRedoService.getInstance().removeSeperatedExeptionRule(
				parent.getId(), data);
	
	}

}
