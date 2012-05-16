package husacct.define.presentation.jpanel.ruledetails;

import husacct.define.presentation.jdialog.ViolationTypesJDialog;
import husacct.define.task.AppliedRuleController;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public abstract class AbstractDetailsJPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = -3429272079796935062L;
	protected AppliedRuleController appliedRuleController;
	protected Logger logger;
	protected boolean isException;

	private JButton configureViolationTypesJButton;
	
	public AbstractDetailsJPanel(AppliedRuleController appliedRuleController){
		super();
		this.appliedRuleController = appliedRuleController;
		this.logger = Logger.getLogger(AbstractDetailsJPanel.class);
		isException = false;
	}
	
	public void initGui(){
		initGui(false);
	}
	
	public void initGui(boolean isUsedAsException){
		try {
			this.removeAll();
			this.setLayout(this.createRuleDetailsLayout());
			this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			setIsUsedAsException(isUsedAsException);
			initViolationTypes();
			initDetails();
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
	
	private void initViolationTypes() {
		if (!isException){
			configureViolationTypesJButton = new JButton("Configure filter");
			configureViolationTypesJButton.addActionListener(this);
			//TODO relocate filter button
			//Dont add it like this. it will mess up the current layout, making it functional but not user friendly
//			GridBagConstraints gbc = new GridBagConstraints(1, 900, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
//			this.add(configureViolationTypesJButton, gbc);
			configureViolationTypesJButton.setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.configureViolationTypesJButton) {
			this.initViolationTypeJDialog();
		}
		
	}
	
	private void initViolationTypeJDialog() {
		ViolationTypesJDialog violationTypesJDialog = new ViolationTypesJDialog();
		violationTypesJDialog.setLocationRelativeTo(this.getRootPane());
		violationTypesJDialog.setVisible(true);
	}

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
		ruleDetails.put("violationTypes", violationTypes);
		return ruleDetails;
	}
	
	public abstract void updateDetails(HashMap<String, Object> ruleDetails);
	
	public void setIsUsedAsException(boolean isException) {
		this.isException = isException;
	}
}
