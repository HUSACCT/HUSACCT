package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

public class ActiveViolationPanel extends javax.swing.JPanel {
	
	private static Logger logger = Logger.getLogger(ActiveViolationPanel.class);
	
	private final DefaultListModel categoryModel;
	private final DefaultListModel ruletypeModel;
	private final String language;
	private final TaskServiceImpl taskServiceImpl;
	private final HashMap<String, List<RuleType>> ruletypes;
	private final List<ActiveRuleType> activeRuletypes;
	private List<ActiveViolationType> activeViolationtypes;
	
	private DefaultTableModel violationtypeModel;
	
	private JButton apply, deselectAll, selectAll;
	private JList categoryJList, ruletypeJList;
	private JScrollPane categoryScrollpane, ruletypeScrollpane,
			violationtypeScrollpane;
	private JTable violationtypeTable;

	public ActiveViolationPanel(TaskServiceImpl taskServiceImpl, HashMap<String, List<RuleType>> ruletypes, String language) {
		
		categoryModel = new DefaultListModel();
		ruletypeModel = new DefaultListModel();
		
		this.taskServiceImpl = taskServiceImpl;
		this.ruletypes = ruletypes;
		this.language = language;
		activeRuletypes = taskServiceImpl.getActiveViolationTypes().get(language);
		
		initComponents();
		loadAfterChange();
	}

    private void initComponents() {

        categoryScrollpane = new JScrollPane();
		categoryJList = new JList();
		ruletypeScrollpane = new JScrollPane();
		violationtypeTable = new JTable();
		violationtypeScrollpane = new JScrollPane();
		ruletypeJList = new JList();
		selectAll = new JButton();
		deselectAll = new JButton();
		apply = new JButton();

		categoryJList.setModel(categoryModel);
		categoryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categoryJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				if(categoryJList.getSelectedIndex() > -1){
					CategoryValueChanged();
				}
			}
		});
		categoryScrollpane.setViewportView(categoryJList);
		
		violationtypeTable.getTableHeader().setReorderingAllowed(false);
		violationtypeTable.setFillsViewportHeight(true);
		violationtypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ruletypeScrollpane.setViewportView(violationtypeTable);
		
		ruletypeJList.setModel(ruletypeModel);
		
		ruletypeJList.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		ruletypeJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				if(ruletypeJList.getSelectedIndex() > -1){
					RuletypeValueChanged();
				}
			}
		});
		violationtypeScrollpane.setViewportView(ruletypeJList);
		
		selectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				SelectAllActionPerformed();
			}
		});
		
		deselectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				DeselectAllActionPerformed();
			}
		});
		
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				ApplyActionPerformed();
			}
		});

		createLayout();
    }
	
	private void createLayout(){
		GroupLayout activeViolationtypeLayout = new GroupLayout(this);
		
		GroupLayout.ParallelGroup horizontalButtonGroup = activeViolationtypeLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false);
		horizontalButtonGroup.addComponent(selectAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(deselectAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(apply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		
		GroupLayout.SequentialGroup horizontalPaneGroup = activeViolationtypeLayout.createSequentialGroup();
		horizontalPaneGroup.addComponent(categoryScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addComponent(violationtypeScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addComponent(ruletypeScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addGroup(horizontalButtonGroup);
		
		activeViolationtypeLayout.setHorizontalGroup(horizontalPaneGroup);
		
		GroupLayout.SequentialGroup verticalButtonGroup = activeViolationtypeLayout.createSequentialGroup();
		verticalButtonGroup.addContainerGap();
		verticalButtonGroup.addComponent(selectAll);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(deselectAll);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(apply);
		verticalButtonGroup.addContainerGap();
		
		GroupLayout.ParallelGroup verticalPaneGroup = activeViolationtypeLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		verticalPaneGroup.addComponent(ruletypeScrollpane);
		verticalPaneGroup.addGroup(verticalButtonGroup);
		verticalPaneGroup.addComponent(categoryScrollpane);
		verticalPaneGroup.addComponent(violationtypeScrollpane);
		
		activeViolationtypeLayout.setVerticalGroup(verticalPaneGroup);
		
		setLayout(activeViolationtypeLayout);
	}
	
	public final void loadAfterChange(){
		setText();
		loadModels();
		loadRuleTypeCategories();
	}
	
	private void setText(){
		categoryJList.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Category")));
		ruletypeJList.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Ruletypes")));
		selectAll.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("SelectAll"));
		deselectAll.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("DeselectAll"));
		apply.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Apply"));
	}
	
	private void loadModels(){
		
		String[] ViolationtypeModelHeaders = {ServiceProvider.getInstance().getControlService().getTranslatedString("Violationtype"), ServiceProvider.getInstance().getControlService().getTranslatedString("Active")};
		violationtypeModel = new DefaultTableModel(ViolationtypeModelHeaders, 0){

			Class<?>[] types = new Class[]{String.class, Boolean.class};
			boolean[] canEdit = new boolean[]{false, true};

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};
		
		
		violationtypeTable.setModel(violationtypeModel);
	}
	
	private void loadRuleTypeCategories() {
		categoryModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			categoryModel.addElement(ServiceProvider.getInstance().getControlService().getTranslatedString(categoryString));
		}
	}
	
	private void SelectAllActionPerformed() {
		for(int i = 0; i < violationtypeModel.getRowCount(); i++){
			violationtypeModel.setValueAt(true, i, 1);
		}
	}
	
	private void DeselectAllActionPerformed() {
		for(int i = 0; i < violationtypeModel.getRowCount(); i++){
			violationtypeModel.setValueAt(false, i, 1);
		}
	}

	private void ApplyActionPerformed() {
		ActiveRuleType activeRuletype = activeRuletypes.get(ruletypeJList.getSelectedIndex());
		for(int i = 0; i < violationtypeModel.getRowCount(); i++){
			boolean test = (Boolean) violationtypeModel.getValueAt(i, 1);
			try{
				activeViolationtypes.get(i).setEnabled(test);
			} catch(IndexOutOfBoundsException outOfBoundsException) {
				logger.error("Something went wrong. Active violationtypes can not be set.");
			}
		}
		activeRuletype.setViolationTypes(activeViolationtypes);
		
		activeRuletypes.remove(ruletypeJList.getSelectedIndex());
		activeRuletypes.add(ruletypeJList.getSelectedIndex(), activeRuletype);

		taskServiceImpl.setActiveViolationTypes(language, activeRuletypes);
	}

	private void CategoryValueChanged() {
		LoadRuletypes((String) categoryJList.getSelectedValue());
	}

	private void LoadRuletypes(String category) {
		ruletypeModel.clear();

		List<RuleType> rules = ruletypes.get(ValidateTranslator.getKey(category));
		for(RuleType ruletype: rules){
			ruletypeModel.addElement(ServiceProvider.getInstance().getControlService().getTranslatedString(ruletype.getKey()));
		}
	}
	
	private void RuletypeValueChanged() {
		LoadViolationtypes((String) ruletypeJList.getSelectedValue());
	}

	private void LoadViolationtypes(String ruletypekey) {
		while(violationtypeModel.getRowCount() > 0){
			violationtypeModel.removeRow(0);
		}

		for (ActiveRuleType ruletypeKey : activeRuletypes) {
			if(ruletypeKey.getRuleType().equals(ValidateTranslator.getKey(ruletypekey))){
				for(ActiveViolationType violationtype : ruletypeKey.getViolationTypes()){
					violationtypeModel.addRow(new Object[]{ServiceProvider.getInstance().getControlService().getTranslatedString(violationtype.getType()), violationtype.isEnabled()});
				}
				activeViolationtypes = ruletypeKey.getViolationTypes();
			}
		}
	}
}
