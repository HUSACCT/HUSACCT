package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.LanguageSeverityConfiguration;
import husacct.validate.presentation.tableModels.ComboBoxTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

public class RuleTypeSeverityPanel extends javax.swing.JPanel {

	private ComboBoxTableModel ruletypeModel;
	
	private final DefaultListModel rtsCategoryModel;
	private final String language;
	private final HashMap<String, List<RuleType>> ruletypes;
	private final LanguageSeverityConfiguration languageSeverityConfiguration;
	
	private TaskServiceImpl taskServiceImpl;
	private JButton Apply, Restore, RestoreAll;
	private JList Category;
	private JScrollPane CategoryScrollpane, RuletypeScrollpane;
	private JTable RuletypeTable;
	
	public RuleTypeSeverityPanel(TaskServiceImpl taskServiceImpl, LanguageSeverityConfiguration languageSeverityConfiguration, HashMap<String, List<RuleType>> ruletypes, String language) {
		rtsCategoryModel = new DefaultListModel();
		
		this.taskServiceImpl = taskServiceImpl;
		this.languageSeverityConfiguration = languageSeverityConfiguration;
		this.language = language;
		this.ruletypes = ruletypes;
		
		initComponents();
		setText();		
	}

    private void initComponents() {
		
		CategoryScrollpane = new JScrollPane();
		Category = new JList();
		RuletypeScrollpane = new JScrollPane();
		RuletypeTable = new JTable();
		Apply = new JButton();
		Restore = new JButton();
		RestoreAll = new JButton();
		
		Category.setModel(rtsCategoryModel);
		Category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Category.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				 if (evt.getValueIsAdjusting()) {
					 return;
				 } 
				rtsCategoryValueChanged();
			}
		});
		CategoryScrollpane.setViewportView(Category);

		RuletypeTable.setFillsViewportHeight(true);
		RuletypeTable.getTableHeader().setReorderingAllowed(false);
		RuletypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		RuletypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				checkRestoreButtonEnabled();
			}
		});
		
		RuletypeScrollpane.setViewportView(RuletypeTable);
		
		Apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsApplyActionPerformed();
			}
		});
		
		Restore.setEnabled(false);
		Restore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsRestoreActionPerformed();
			}
		});
		
		RestoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsRestoreAllActionPerformed();
			}
		});

		GroupLayout ruletypeSeverityLayout = new GroupLayout(this);
		ruletypeSeverityLayout.setHorizontalGroup(
			ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(ruletypeSeverityLayout.createSequentialGroup()
					.addComponent(CategoryScrollpane)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(RuletypeScrollpane)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(Restore)
						.addComponent(RestoreAll)
						.addComponent(Apply)
					)
					.addContainerGap()
				)
		);
		ruletypeSeverityLayout.setVerticalGroup(
			ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(CategoryScrollpane)
				.addComponent(RuletypeScrollpane)
				.addGroup(ruletypeSeverityLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(Restore)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(RestoreAll)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(Apply)
					.addContainerGap())
		);
		
		this.setLayout(ruletypeSeverityLayout);
    }
	
	public void setText(){
		Category.setBorder(BorderFactory.createTitledBorder(ValidateTranslator.getValue("Category")));
		Apply.setText(ValidateTranslator.getValue("Apply"));
		Restore.setText(ValidateTranslator.getValue("RestoreToDefault"));
		RestoreAll.setText(ValidateTranslator.getValue("RestoreAllToDefault"));
		
		loadModel();
	}
	
	public void loadModel(){
		String[] ruletypeColumnNames = {ValidateTranslator.getValue("Ruletype"), ValidateTranslator.getValue("Severity")};
		ruletypeModel = new ComboBoxTableModel(ruletypeColumnNames, 0, languageSeverityConfiguration.getSeverityNames());
		ruletypeModel.setTypes(new Class[]{String.class, Severity.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});
		
		RuletypeTable.setModel(ruletypeModel);
		
		TableColumnModel tcm = RuletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());
		
		loadRuleTypeCategories();
	}
	
	private void rtsRestoreActionPerformed() {		
		taskServiceImpl.restoreToDefault(language, ValidateTranslator.getKey((String) ruletypeModel.getValueAt(RuletypeTable.getSelectedRow(), 0)));
		rtsCategoryValueChanged();
	}

	private void rtsRestoreAllActionPerformed() {
		taskServiceImpl.restoreAllToDefault(language);
		rtsCategoryValueChanged();
	}

	private void rtsApplyActionPerformed() {
		checkRestoreButtonEnabled();
		updateRuletypeSeverities();
	}
	
	
	private void rtsCategoryValueChanged() {
		loadRuleTypes((String) Category.getSelectedValue());
	}
	
	private void loadRuleTypeCategories() {
		rtsCategoryModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			rtsCategoryModel.addElement(ValidateTranslator.getValue(categoryString));
		}
	}
	
	private void loadRuleTypes(String category) {
		ruletypeModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			if (ValidateTranslator.getValue(categoryString).equals(category)){
				List<RuleType> rules = ruletypes.get(categoryString);
				for(RuleType ruletype: rules){
					Severity severity;
					try{
						severity = taskServiceImpl.getSeverityFromKey(language, ruletype.getKey());
					} catch (Exception e){
						severity = taskServiceImpl.getAllSeverities().get(0);
					}
					ruletypeModel.addRow(new Object[]{ValidateTranslator.getValue(ruletype.getKey()), severity});
				}
			}

		}
		ruletypeModel.checkValuesAreValid();	
	}
	
	private void updateRuletypeSeverities() {
		HashMap<String, Severity> map = new HashMap<String, Severity>();

		for(int i = 0; i < ruletypeModel.getRowCount(); i++){
			map.put(ValidateTranslator.getKey((String) ruletypeModel.getValueAt(i, 0)), (Severity) ruletypeModel.getValueAt(i, 1));
		}
		
		taskServiceImpl.updateSeverityPerType(map, language);
	}
	
	private void checkRestoreButtonEnabled(){
		if(RuletypeTable.getSelectedRow() > -1){
			Restore.setEnabled(true);
		} else{
			Restore.setEnabled(false);
		}
	}
}
