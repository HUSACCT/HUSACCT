package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
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
		loadAfterChange();
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
		
		GroupLayout.ParallelGroup horizontalButtonLayout = ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false);
		horizontalButtonLayout.addComponent(Restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonLayout.addComponent(RestoreAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonLayout.addComponent(Apply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		
		GroupLayout.SequentialGroup horizontalPaneLayout = ruletypeSeverityLayout.createSequentialGroup();
		horizontalPaneLayout.addComponent(CategoryScrollpane);
		horizontalPaneLayout.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneLayout.addComponent(RuletypeScrollpane);
		horizontalPaneLayout.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneLayout.addGroup(horizontalButtonLayout);
		horizontalPaneLayout.addContainerGap();
		
		ruletypeSeverityLayout.setHorizontalGroup(horizontalPaneLayout);
		
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
					.addContainerGap()
				)
		);
		
		setLayout(ruletypeSeverityLayout);
    }
	
	public final void loadAfterChange(){
		setText();
		loadModel();
		loadRuleTypeCategories();
	}
	
	private void setText(){
		Category.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Category")));
		Apply.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Apply"));
		Restore.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("RestoreToDefault"));
		RestoreAll.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("RestoreAllToDefault"));
	}
	
	private void loadModel(){
		String[] ruletypeColumnNames = {ServiceProvider.getInstance().getControlService().getTranslatedString("Ruletype"), ServiceProvider.getInstance().getControlService().getTranslatedString("Severity")};
		ruletypeModel = new ComboBoxTableModel(ruletypeColumnNames, 0, languageSeverityConfiguration.getSeverityNames());
		ruletypeModel.setTypes(new Class[]{String.class, Severity.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});
		
		RuletypeTable.setModel(ruletypeModel);
		
		TableColumnModel tcm = RuletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());
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
			rtsCategoryModel.addElement(ServiceProvider.getInstance().getControlService().getTranslatedString(categoryString));
		}
	}
	
	private void loadRuleTypes(String category) {
		ruletypeModel.clear();
		List<RuleType> rules = ruletypes.get(category);
		for(RuleType ruletype: rules){
			Severity severity;
			try{
				severity = taskServiceImpl.getSeverityFromKey(language, ruletype.getKey());
			} catch (Exception e){
				severity = taskServiceImpl.getAllSeverities().get(0);
			}
			ruletypeModel.addRow(new Object[]{ServiceProvider.getInstance().getControlService().getTranslatedString(ruletype.getKey()), severity});
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
