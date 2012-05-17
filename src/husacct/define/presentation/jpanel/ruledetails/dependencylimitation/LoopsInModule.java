package husacct.define.presentation.jpanel.ruledetails.dependencylimitation;

import java.util.HashMap;

import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.task.AppliedRuleController;

public class LoopsInModule extends AbstractDetailsJPanel{
	private static final long serialVersionUID = 7498639445874148975L;
	public static final String ruleTypeKey = "LoopsInModule";
	
	public LoopsInModule(AppliedRuleController appliedRuleController) {
		super(appliedRuleController);
	}

	@Override
	public void initDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, Object> saveToHashMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDetails(HashMap<String, Object> ruleDetails) {
		// TODO Auto-generated method stub
		
	}

}
