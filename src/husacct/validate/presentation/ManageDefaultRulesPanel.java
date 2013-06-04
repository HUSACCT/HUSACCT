package husacct.validate.presentation;

import java.util.HashMap;

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
	private RuleTypeDTO[] allowedRules, currentlyDefaultRules;
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
		for(ModuleTypes modules : ModuleTypes.values()){
			rtsComponentModel.addElement(new DataLanguageHelper(modules.toString()));
		}
		
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if(listSelectionEvent.getValueIsAdjusting()){
					loadTable(components.getSelectedIndex());
				}
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
			public void tableChanged(TableModelEvent e) {
				if(e.getColumn() == 1){
					System.out.println("ValidateService -> SetDefaultRule(" + componentList[components.getSelectedIndex()] + ", " + allowedRulesMap.get(e.getFirstRow()) + ", " + tableModel.getValueAt(e.getFirstRow(), 1) + ")");
					//SetDefaultRule(componentList[components.getSelectedIndex()], allowedRulesMap.get(e.getFirstRow()), tableModel.getValueAt(e.getFirstRow(), 1));
				}
			}
		});
		ruleTable = new JTable(tableModel){
			private static final long serialVersionUID = 1L;

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
			
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return (column == 1);
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
		this.setLayout(layout);
	}
	
	private void loadTable(int listItem){
		tableModel.setRowCount(0);
		allowedRules = ServiceProvider.getInstance().getValidateService().getAllowedRuleTypesOfModule(componentList[listItem]);
		currentlyDefaultRules = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(componentList[listItem]);
		allowedRulesMap = new HashMap<Integer, String>();
		int count = 0;
		
		for(RuleTypeDTO allowedRule : allowedRules){
			String curRule = allowedRule.key;
			allowedRulesMap.put(count, allowedRule.key);
			count++;
			Boolean isDefault = false;
			for(RuleTypeDTO defaultRule : currentlyDefaultRules)
				if(defaultRule.key.equals(curRule))
					isDefault = true;
			tableModel.addRow(new Object[]{curRule, isDefault});
		}
	}
}