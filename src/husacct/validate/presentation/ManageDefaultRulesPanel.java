package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.module.ModuleTypes;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ManageDefaultRulesPanel extends JPanel {
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private static final long serialVersionUID = 1L;
	private final DefaultListModel<DataLanguageHelper> rtsComponentModel;
	private JList<DataLanguageHelper> components;
	private JScrollPane componentScrollpane, rulesScrollpane;
	private JTable ruleTable;
	private DefaultTableModel tableModel;
	private RuleTypeDTO[] allowedRules, currentlyDefaultRules;
	private String[] componentList = {"Component", "ExternalLibrary", "Facade", "Layer", "SubSystem"};

	// ==================================
	// Init panel
	// ==================================
	public ManageDefaultRulesPanel(){
		// ===== Component selector =====
		componentScrollpane = new JScrollPane();
		components = new JList<DataLanguageHelper>();
		rtsComponentModel = new DefaultListModel<DataLanguageHelper>();
		for(ModuleTypes modules : ModuleTypes.values()){
			rtsComponentModel.addElement(new DataLanguageHelper(modules.toString()));
		}
		
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(listSelectionEvent.getValueIsAdjusting()){
					System.out.println("Selected item: " + components.getSelectedIndex());
					tableModel.setRowCount(0);

					allowedRules = ServiceProvider.getInstance().getValidateService().getAllowedRuleTypesOfModule(componentList[components.getSelectedIndex()]);
					currentlyDefaultRules = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(componentList[components.getSelectedIndex()]);
					
					for(RuleTypeDTO allowedRule : allowedRules){
						String curRule = allowedRule.key;
						Boolean isDefault = false;
						for(RuleTypeDTO defaultRule : currentlyDefaultRules)
							if(defaultRule.key.equals(curRule))
								isDefault = true;
						tableModel.addRow(new Object[]{curRule, isDefault});
					}
				}
	    	}
		};
	    components.addListSelectionListener(listSelectionListener);
	    
		components.setModel(rtsComponentModel);
		components.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (!rtsComponentModel.isEmpty()) {
			components.setSelectedIndex(0);
		}
		componentScrollpane.setViewportView(components);
		
		// ===== Rule checkbox table =====		
		tableModel = new DefaultTableModel();
		tableModel.addColumn(localeService.getTranslatedString("Rule"));
		tableModel.addColumn(localeService.getTranslatedString("RuleIsDefault"));
		ruleTable = new JTable(tableModel){
			@Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
		};
		rulesScrollpane = new JScrollPane(ruleTable);
		
		// ===== Create layout =====
		GroupLayout layout = new GroupLayout(this);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(componentScrollpane, 200, 200, 200)
				.addComponent(rulesScrollpane)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(componentScrollpane)
					.addComponent(rulesScrollpane)
			)
		);
		this.setLayout(layout);
	}
}