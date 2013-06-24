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
	}
	
	private Object[] convertOriginalValues(AppliedRuleStrategy rule) {
		    String ruleTypeKey = rule.getRuleType();
		     String description=rule.getDescription();
		    String[] dependencies = rule.getDependencies();
		     String regex=rule.getRegex();
		    ModuleStrategy ModuleStrategyFrom = rule.getModuleFrom();
		    ModuleStrategy ModuleStrategyTo =rule.getModuleTo();
		    boolean enabled = rule.isEnabled();
		
		    Object[] o ={ruleTypeKey,description,dependencies,regex,ModuleStrategyFrom,ModuleStrategyTo,enabled};
		    return o;
		    		
	}

	@Override
	public void undo() {
		

	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub

	}

}
