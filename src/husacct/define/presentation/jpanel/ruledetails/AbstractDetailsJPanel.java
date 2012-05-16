package husacct.define.presentation.jpanel.ruledetails;

import husacct.define.task.AppliedRuleController;

import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public abstract class AbstractDetailsJPanel extends JPanel{
	
	private static final long serialVersionUID = -3429272079796935062L;
	protected AppliedRuleController appliedRuleController;
	protected Logger logger;
	
	public AbstractDetailsJPanel(AppliedRuleController appliedRuleController){
		super();
		this.appliedRuleController = appliedRuleController;
		this.logger = Logger.getLogger(RuleDetailsJPanel.class);
	}
	
	public void initGui(){
		try {
			this.removeAll();
			this.setLayout(this.createRuleDetailsLayout());
			this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			this.initDetails();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	protected GridBagLayout createRuleDetailsLayout() {
		GridBagLayout ruleDetailsLayout = new GridBagLayout();
		ruleDetailsLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
		ruleDetailsLayout.rowHeights = new int[] { 23, 23, 29, 7 };
		ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.1 };
		ruleDetailsLayout.columnWidths = new int[] { 132, 7 };
		return ruleDetailsLayout;
	}
	
	public abstract void initDetails();
	
	public abstract HashMap<String, Object> saveToHashMap();

	protected HashMap<String, Object> saveDefaultDataToHashMap() {
		HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
		long moduleFromId = -1;
		long moduleToId = -1;
		boolean enabled = true;
		String description = "";
		String regex = "";
		String[] violationTypes = {};
		
		ruleDetails.put("moduleFromId", moduleFromId);
		ruleDetails.put("moduleToId", moduleToId);
		ruleDetails.put("enabled", enabled);
		ruleDetails.put("description", description);
		ruleDetails.put("regex", regex);
		ruleDetails.put("regex", violationTypes);
		return ruleDetails;
	}
	
	public abstract void updateDetails(HashMap<String, Object> ruleDetails);
	
}
