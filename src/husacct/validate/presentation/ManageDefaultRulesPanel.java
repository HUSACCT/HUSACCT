package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.moduletype.ModuleTypes;

import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ManageDefaultRulesPanel extends JPanel {
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private static final long serialVersionUID = 1L;
	private final DefaultListModel<DataLanguageHelper> rtsComponentModel;
	private JList<DataLanguageHelper> components;
	private JScrollPane componentScrollpane, rulesScrollpane;
	private JTable ruleTable;
	private DefaultTableModel tableModel;
	private RuleTypeDTO[] allowedRules, currentDefaultRules;
	private String[] componentList = {"Component", "ExternalLibrary", "Facade", "Layer", "SubSystem"};
	private HashMap<Integer, String> allowedRulesMap;

	// ==================================
	// Init panel
	// ==================================
	public ManageDefaultRulesPanel(){
		// ===== Component selector =====
		componentScrollpane = new JScrollPane();
		components = new JList<DataLanguageHelper>();
		rtsComponentModel = new DefaultListModel<DataLanguageHelper>();
		for(ModuleTypes modules : ModuleTypes.values())
			rtsComponentModel.addElement(new DataLanguageHelper(modules.toString()));
		
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(listSelectionEvent.getValueIsAdjusting()) loadTable(components.getSelectedIndex());
	    	}
		};
	    components.addListSelectionListener(listSelectionListener);
	    
		components.setModel(rtsComponentModel);
		components.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		componentScrollpane.setViewportView(components);
		
		// ===== Rule checkbox table =====		
		tableModel = new DefaultTableModel();
		tableModel.addColumn(localeService.getTranslatedString("Rule"));
		tableModel.addColumn(localeService.getTranslatedString("RuleIsDefault"));
		tableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int gc = e.getColumn();
				if(e.getColumn() == 1){
					String moduleType = componentList[components.getSelectedIndex()];
					String ruleTypeKey = allowedRulesMap.get(e.getFirstRow());
					Object stringValue = tableModel.getValueAt(e.getFirstRow(), 1);
					int row = e.getFirstRow();
					boolean value = Boolean.parseBoolean(stringValue.toString());
					System.out.println("ValidateService -> SetDefaultRule(" + componentList[components.getSelectedIndex()] + ", " + allowedRulesMap.get(e.getFirstRow()) + ", " + tableModel.getValueAt(e.getFirstRow(), 1) + ")");
					ServiceProvider.getInstance().getValidateService().setDefaultRuleTypeOfModule(moduleType, ruleTypeKey, value);
				}
			}
		});

		ruleTable = new JTable(tableModel){
			private static final long serialVersionUID = 1L;
			
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 1;
		    }
		};
		ruleTable.getColumnModel().getColumn(1).setMaxWidth(100);
		ruleTable.getColumnModel().getColumn(1).setMinWidth(100);
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
		setLayout(layout);
	}
	
	private void loadTable(int listItem){
		tableModel.setRowCount(0);
		allowedRules = ServiceProvider.getInstance().getValidateService().getAllowedRuleTypesOfModule(componentList[listItem]);
		currentDefaultRules = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(componentList[listItem]);
		allowedRulesMap = new HashMap<Integer, String>();
		int count = 0;
		
		for(RuleTypeDTO allowedRule : allowedRules){
			String curRule = allowedRule.key;
			String curRuleTranslated = localeService.getTranslatedString(curRule);
			allowedRulesMap.put(count, curRuleTranslated);
			count++;
			Boolean isDefault = false;
			for(RuleTypeDTO defaultRule : currentDefaultRules)
				if(defaultRule.key.equals(curRule))
					isDefault = true;
			tableModel.addRow(new Object[]{curRuleTranslated, isDefault});
		}
	}
}