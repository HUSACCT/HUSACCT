package husacct.define.domain.services.stateservice.state.appliedrule;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class EditAppliedRuleCommand implements Istate {
  private AppliedRuleStrategy data ;
  private Object[] originalValues;
  private Object[] newValues;
	public EditAppliedRuleCommand(AppliedRuleStrategy rule ,Object[] newData) {
	this.data=rule;
	this.originalValues=convertOriginalValues(rule);
	this.newValues=newData;
	}
	
	private Object[] convertOriginalValues(AppliedRuleStrategy rule) {
		    String ruleTypeKey = rule.getRuleTypeKey();
		     String description=rule.getDescription();
		    String[] dependencies = rule.getDependencyTypes();
		     String regex=rule.getRegex();
		    ModuleStrategy ModuleStrategyFrom = rule.getModuleFrom();
		    ModuleStrategy ModuleStrategyTo =rule.getModuleTo();
		    boolean enabled = rule.isEnabled();
		
		    Object[] o ={ruleTypeKey,description,dependencies,regex,ModuleStrategyFrom,ModuleStrategyTo,enabled};
		    return o;
		    		
	}

	@Override
	public void undo() {
		UndoRedoService.getInstance().editAppliedRule(data.getId(),originalValues);

	}

	@Override
	public void redo() {
		UndoRedoService.getInstance().editAppliedRule(data.getId(),newValues);

	}

}
