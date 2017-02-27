package husacct.define.domain.services.stateservice.state.appliedrule;

import java.util.ArrayList;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class ExceptionAddRuleCommand implements Istate {

	private ArrayList<AppliedRuleStrategy> data;
	private AppliedRuleStrategy parent;

	public ExceptionAddRuleCommand(AppliedRuleStrategy parent,
			ArrayList<AppliedRuleStrategy> rules) {
		this.data = rules;
		this.parent = parent;
	}

	@Override
	public void undo() {
		UndoRedoService.getInstance().removeSeperatedExeptionRule(
				parent.getId(), data);

	}

	@Override
	public void redo() {
		UndoRedoService.getInstance().addSeperatedExeptionRule(parent.getId(),
				data);
		

	}

}
