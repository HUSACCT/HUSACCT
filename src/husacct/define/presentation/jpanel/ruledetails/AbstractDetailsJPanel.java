package husacct.define.presentation.jpanel.ruledetails;

import husacct.ServiceProvider;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.presentation.jdialog.ViolationTypesJDialog;
import husacct.define.task.AppliedRuleController;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public abstract class AbstractDetailsJPanel extends JPanel implements
ActionListener {

	private static final long serialVersionUID = -3429272079796935062L;
	protected AppliedRuleController appliedRuleController;
	protected int componentCount;
	private JButton configureViolationTypesJButton;
	protected boolean showconfigureViolationTypesJButton = false;
	private String ruleTypeKey = ""; // Needed in case showconfigureViolationTypesJButton = true (e.g., NamingConvention and VisibilityConvention)
	protected boolean violationTypesAreLanguageDependent = true;
	
	protected boolean isException = false;
	protected Logger logger;

	protected ViolationTypesJDialog violationTypesJDialog;

	public AbstractDetailsJPanel(AppliedRuleController appliedRuleController) {
		super();
		this.appliedRuleController = appliedRuleController;
		logger = Logger.getLogger(AbstractDetailsJPanel.class);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == configureViolationTypesJButton) {
			initViolationTypeJDialog();
		}

	}

	@Override
	public void add(Component comp, Object constraint) {
		super.add(comp, constraint);
		componentCount++;
	}

	protected GridBagLayout createRuleDetailsLayout() {
		GridBagLayout ruleDetailsLayout = new GridBagLayout();
		ruleDetailsLayout.columnWeights = new double[] { 0.0, 0.1 };
		ruleDetailsLayout.columnWidths = new int[] { 132, 7 };
		return ruleDetailsLayout;
	}

	public abstract boolean hasValidData();

	public abstract void initDetails();

	public void initGui() {
		initGui(false);
	}

	public void initGui(boolean isUsedAsException) {
		try {
			removeAll();
			setLayout(createRuleDetailsLayout());
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			setIsUsedAsException(isUsedAsException);
			initDetails();
			initViolationTypes();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void initViolationTypeJDialog() {
		if (violationTypesJDialog == null) {
			violationTypesJDialog = new ViolationTypesJDialog(appliedRuleController, ruleTypeKey);
		}
		violationTypesJDialog.initGUI();
		DialogUtils.alignCenter(violationTypesJDialog);
		violationTypesJDialog.setVisible(true);
	}

	private void initViolationTypes() {
		configureViolationTypesJButton = new JButton(ServiceProvider
				.getInstance().getLocaleService()
				.getTranslatedString("ConfigureFilter"));
		configureViolationTypesJButton.addActionListener(this);
		violationTypesJDialog = new ViolationTypesJDialog(appliedRuleController, ruleTypeKey);

		GridBagConstraints gbc = new GridBagConstraints(1, componentCount++, 1,
				1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		this.add(configureViolationTypesJButton, gbc);

		if (violationTypesAreLanguageDependent && !appliedRuleController.isAnalysed()) {
			configureViolationTypesJButton.setEnabled(false);
			configureViolationTypesJButton.setToolTipText(ServiceProvider
					.getInstance().getLocaleService()
					.getTranslatedString("NeedToAnalyseFirst"));
		} else {
			configureViolationTypesJButton.setEnabled(true);
			configureViolationTypesJButton.setToolTipText(ServiceProvider
					.getInstance().getLocaleService()
					.getTranslatedString("ValidateOnSpecificDependencies"));
		}

		configureViolationTypesJButton.setVisible(showconfigureViolationTypesJButton);
	}

	private HashMap<String, Object> saveDefaultDataToHashMap() {
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

	public HashMap<String, Object> saveToHashMap() {
		HashMap<String, Object> hashMap = saveDefaultDataToHashMap();
		hashMap.put("dependencies", violationTypesJDialog.save());
		return hashMap;
	}

	public void setIsUsedAsException(boolean isException) {
		this.isException = isException;
	}

	public void updateDetails(HashMap<String, Object> ruleDetails) {
		violationTypesJDialog.load(ruleDetails);
	}

	protected void setRuleTypeKey(String ruleTypeKey) {
		this.ruleTypeKey = ruleTypeKey;
	}

}
