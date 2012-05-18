package husacct.validate.presentation.languageSeverityConfiguration;

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

public class ActiveViolationPanel extends javax.swing.JPanel {
	
	private final DefaultListModel CategoryModel;
	private final DefaultListModel RuletypeModel;
	private final String language;
	private final TaskServiceImpl taskServiceImpl;
	private final HashMap<String, List<RuleType>> ruletypes;
	
	private DefaultTableModel ViolationtypeModel;
	
	private JButton Apply, DeselectAll, SelectAll;
	private JList Category, Ruletype;
	private JScrollPane CategoryScrollpane, RuletypeScrollpane,
			ViolationtypeScrollpane;
	private JTable ViolationtypeTable;

	public ActiveViolationPanel(TaskServiceImpl taskServiceImpl, HashMap<String, List<RuleType>> ruletypes, String language) {
		
		CategoryModel = new DefaultListModel();
		RuletypeModel = new DefaultListModel();
		
		this.taskServiceImpl = taskServiceImpl;
		this.ruletypes = ruletypes;
		this.language = language;
		
		initComponents();
		setText();
	}

    private void initComponents() {

        CategoryScrollpane = new JScrollPane();
		Category = new JList();
		RuletypeScrollpane = new JScrollPane();
		ViolationtypeTable = new JTable();
		ViolationtypeScrollpane = new JScrollPane();
		Ruletype = new JList();
		SelectAll = new JButton();
		DeselectAll = new JButton();
		Apply = new JButton();

		Category.setModel(CategoryModel);
		Category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Category.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				avtCategoryValueChanged();
			}
		});
		CategoryScrollpane.setViewportView(Category);
		
		ViolationtypeTable.getTableHeader().setReorderingAllowed(false);
		ViolationtypeTable.setFillsViewportHeight(true);
		ViolationtypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		RuletypeScrollpane.setViewportView(ViolationtypeTable);
		
		Ruletype.setModel(RuletypeModel);
		
		Ruletype.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		Ruletype.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				avtViolationtypeTableValueChanged();
			}
		});
		ViolationtypeScrollpane.setViewportView(Ruletype);
		
		SelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtSelectAllActionPerformed();
			}
		});
		
		DeselectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtDeselectAllActionPerformed();
			}
		});
		
		Apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtApplyActionPerformed();
			}
		});

		GroupLayout activeViolationtypeLayout = new GroupLayout(
				this);
		activeViolationtypeLayout.setHorizontalGroup(
			activeViolationtypeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(activeViolationtypeLayout.createSequentialGroup()
					.addComponent(CategoryScrollpane)
//					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(ViolationtypeScrollpane)
//					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(RuletypeScrollpane)
					.addGap(6)
					.addGroup(activeViolationtypeLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(SelectAll)
						.addComponent(DeselectAll)
						.addComponent(Apply)
					)
				)
		);
		activeViolationtypeLayout.setVerticalGroup(
			activeViolationtypeLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(RuletypeScrollpane)
				.addGroup(GroupLayout.Alignment.LEADING, activeViolationtypeLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(SelectAll)
//					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(DeselectAll)
//					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(Apply)
					.addContainerGap())
				.addComponent(CategoryScrollpane)
				.addComponent(ViolationtypeScrollpane)
		);
		
		this.setLayout(activeViolationtypeLayout);
    }
	
	public void setText(){
		Category.setBorder(BorderFactory.createTitledBorder(ValidateTranslator.getValue("Category")));
		Ruletype.setBorder(BorderFactory.createTitledBorder(ValidateTranslator.getValue("Ruletypes")));
		SelectAll.setText(ValidateTranslator.getValue("SelectAll"));
		DeselectAll.setText(ValidateTranslator.getValue("DeselectAll"));
		Apply.setText(ValidateTranslator.getValue("Apply"));
		
		loadModels();
	}
	
	public void loadModels(){
		
		String[] avtViolationtypeModelHeaders = {ValidateTranslator.getValue("Violationtype"), ValidateTranslator.getValue("Active")};
		ViolationtypeModel = new DefaultTableModel(avtViolationtypeModelHeaders, 0){

			private static final long serialVersionUID = -2752815747553087143L;

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
		
		
		ViolationtypeTable.setModel(ViolationtypeModel);
		loadRuleTypeCategories();
	}
	
	private void loadRuleTypeCategories() {
		CategoryModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			CategoryModel.addElement(ValidateTranslator.getValue(categoryString));
		}
	}
	
	private void avtSelectAllActionPerformed() {
		for(int i = 0; i < ViolationtypeModel.getRowCount(); i++){
			ViolationtypeModel.setValueAt(true, i, 1);
		}
	}
	
	private void avtDeselectAllActionPerformed() {
		for(int i = 0; i < ViolationtypeModel.getRowCount(); i++){
			ViolationtypeModel.setValueAt(false, i, 1);
		}
	}

	private void avtApplyActionPerformed() {
		
		
		taskServiceImpl.setActiveViolationTypes(language, null);
	}

	private void avtCategoryValueChanged() {
		avtLoadRuletypes((String) Category.getSelectedValue());
	}

	private void avtViolationtypeTableValueChanged() {
		avtLoadViolationtypes((String) Category.getSelectedValue(), (String) Ruletype.getSelectedValue());
	}

	private void avtLoadRuletypes(String category) {
		RuletypeModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			if (ValidateTranslator.getValue(categoryString).equals(category)){
				List<RuleType> rules = ruletypes.get(categoryString);
				for(RuleType ruletype: rules){
					RuletypeModel.addElement(ValidateTranslator.getValue(ruletype.getKey()));
				}
			}

		}
//		ruletypeModel.checkValuesAreValid();
	}

	private void avtLoadViolationtypes(String category, String ruletypekey) {
		while(ViolationtypeModel.getRowCount() > 0){
			ViolationtypeModel.removeRow(0);
		}

		for (ActiveRuleType ruletypeKey : taskServiceImpl.getActiveViolationTypes().get(language)) {
			
			if(ruletypeKey.getRuleType().equals(ValidateTranslator.getKey(ruletypekey))){
				for(ActiveViolationType violationtype : ruletypeKey.getViolationTypes()){

					ViolationtypeModel.addRow(new Object[]{ValidateTranslator.getValue(violationtype.getType()), violationtype.isEnabled()});
				}
			}
		}
	}
}
