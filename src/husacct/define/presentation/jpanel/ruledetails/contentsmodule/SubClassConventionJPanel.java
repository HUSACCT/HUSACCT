package husacct.define.presentation.jpanel.ruledetails.contentsmodule;

import java.util.HashMap;

import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.task.AppliedRuleController;

public class SubClassConventionJPanel extends AbstractDetailsJPanel{
	private static final long serialVersionUID = 4872800108665922972L;
	public static final String ruleTypeKey = "SubClassConvention";

	public SubClassConventionJPanel(AppliedRuleController appliedRuleController) {
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
