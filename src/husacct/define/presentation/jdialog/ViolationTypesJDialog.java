package husacct.define.presentation.jdialog;

import husacct.common.dto.ViolationTypeDTO;
import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.task.AppliedRuleController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ViolationTypesJDialog extends JDialog{

	private static final long serialVersionUID = 6413960215557327449L;
	private HashMap<String, JCheckBox> violationCheckBoxHashMap;
	protected AppliedRuleController appliedRuleController;
	private JPanel mainPanel;
	
	public ViolationTypesJDialog(AppliedRuleController appliedRuleController) {
		super();
		this.appliedRuleController = appliedRuleController;
		violationCheckBoxHashMap = new HashMap<String, JCheckBox>();
		initDetails();
	}
	
	private void initDetails(){
		//TODO imo the DTO should not be known in presentation layer
		String selectedRuleTypeKey = this.appliedRuleController.getSelectedRuleTypeKey();
		ArrayList<ViolationTypeDTO> violationTypeDtoList = this.appliedRuleController.getViolationTypesByRuleType(selectedRuleTypeKey);
		
		for (ViolationTypeDTO vt : violationTypeDtoList){
			JCheckBox jCheckBox = new JCheckBox(DefineTranslator.translate(vt.key));
			jCheckBox.setSelected(vt.isDefault);
			violationCheckBoxHashMap.put(vt.key, jCheckBox);
		}
	}
	
	public void initGUI(){
		try {
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Violation Types");
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/husacct.png")).getImage());
			
			getContentPane().add(this.createViolationPanel(), BorderLayout.CENTER);
			
			this.setResizable(false);
			this.pack();
			this.setSize(300, 300);
			this.setModal(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Component createViolationPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(this.createViolationsLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		
		Set<String> collection = violationCheckBoxHashMap.keySet();
	    //Iterate through HashMap values iterator
	    for(String key : collection) {
	    	JCheckBox currentCheckBox = violationCheckBoxHashMap.get(key);
	    	mainPanel.add(currentCheckBox, gridBagConstraints);
	    	gridBagConstraints.gridy++;
	  	}
	    mainPanel.setVisible(true);
		return mainPanel;
	}

	private GridBagLayout createViolationsLayout() {
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
		mainPanelLayout.rowHeights = new int[] { 30, 23, 6, 0 };
		mainPanelLayout.columnWeights = new double[] { 0.0, 0.1 };
		mainPanelLayout.columnWidths = new int[] { 132, 7 };
		return mainPanelLayout;
	}

	public String[] save() {	
		ArrayList<String> dependencyList = new ArrayList<String>();
		
	    Set<String> collection = violationCheckBoxHashMap.keySet();
	    //iterate through HashMap values iterator
	    for(String key : collection) {
	    	JCheckBox currentCheckBox = violationCheckBoxHashMap.get(key);
	    	if (currentCheckBox.isSelected()){
	    		dependencyList.add(key);
	    	}
	  	}
		
		String[] dependencies = dependencyList.toArray(new String[dependencyList.size()]);
		return dependencies;
	}
}
