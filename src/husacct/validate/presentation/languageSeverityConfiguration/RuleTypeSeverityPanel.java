package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.DataLanguageHelper;
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

	private static final long serialVersionUID = 5947125752371446966L;

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
		CategoryScrollpane.setViewportView(Category);

		RuletypeTable.setFillsViewportHeight(true);
		RuletypeTable.getTableHeader().setReorderingAllowed(false);
		RuletypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		RuletypeScrollpane.setViewportView(RuletypeTable);
		
		Restore.setEnabled(false);
		
		Category.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				 if (evt.getValueIsAdjusting()) {
					 return;
				 } 
				rtsCategoryValueChanged();
			}
		});
		
		RuletypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				checkRestoreButtonEnabled();
			}
		});
		
		Apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsApplyActionPerformed();
			}
		});
		
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

		createLayout();
    }
	
	private void createLayout(){
		GroupLayout ruletypeSeverityLayout = new GroupLayout(this);
		
		GroupLayout.ParallelGroup horizontalButtonGroup = ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false);
		horizontalButtonGroup.addComponent(Restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(RestoreAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(Apply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		
		GroupLayout.SequentialGroup horizontalPaneGroup = ruletypeSeverityLayout.createSequentialGroup();
		horizontalPaneGroup.addComponent(CategoryScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addComponent(RuletypeScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addGroup(horizontalButtonGroup);
		horizontalPaneGroup.addContainerGap();
		ruletypeSeverityLayout.setHorizontalGroup(horizontalPaneGroup);
		
		GroupLayout.SequentialGroup verticalButtonGroup = ruletypeSeverityLayout.createSequentialGroup();
		verticalButtonGroup.addContainerGap();
		verticalButtonGroup.addComponent(Restore);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(RestoreAll);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(Apply);
		verticalButtonGroup.addContainerGap();
		
		GroupLayout.ParallelGroup verticalPaneGroup = ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		verticalPaneGroup.addComponent(CategoryScrollpane);
		verticalPaneGroup.addComponent(RuletypeScrollpane);
		verticalPaneGroup.addGroup(verticalButtonGroup);
		
		ruletypeSeverityLayout.setVerticalGroup(verticalPaneGroup);
		
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
		ruletypeModel.setTypes(new Class[]{DataLanguageHelper.class, Severity.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});
		
		RuletypeTable.setModel(ruletypeModel);
		
		TableColumnModel tcm = RuletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());
	}
	
	private void rtsRestoreActionPerformed() {		
		taskServiceImpl.restoreToDefault(language, ((DataLanguageHelper) ruletypeModel.getValueAt(RuletypeTable.getSelectedRow(), 0)).key);
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
		loadRuleTypes(((DataLanguageHelper) Category.getSelectedValue()).key);
	}
	
	private void loadRuleTypeCategories() {
		rtsCategoryModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			rtsCategoryModel.addElement(new DataLanguageHelper(categoryString));
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
			ruletypeModel.addRow(new Object[]{new DataLanguageHelper(ruletype.getKey()), severity});
		}
		ruletypeModel.checkValuesAreValid();	
	}
	
	private void updateRuletypeSeverities() {
		HashMap<String, Severity> map = new HashMap<String, Severity>();

		for(int i = 0; i < ruletypeModel.getRowCount(); i++){
		String key =	((DataLanguageHelper) ruletypeModel.getValueAt(i, 0)).key;
			map.put(key, (Severity) ruletypeModel.getValueAt(i, 1));
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
