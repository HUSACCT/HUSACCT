package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.iternal_tranfer_objects.ConfigurationRuleTypeDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.DataLanguageHelper;
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
	
	private final DefaultListModel rtsCategoryModel;
	private final String language;
	private final HashMap<String, List<RuleType>> ruletypes;
	
	private List<Severity> severities;
	private TaskServiceImpl taskServiceImpl;
	private ComboBoxTableModel ruletypeModel;
	private JButton apply, restore, restoreAll;
	private JList category;
	private JScrollPane categoryScrollpane, ruletypeScrollpane;
	private JTable ruletypeTable;
	
	public RuleTypeSeverityPanel(TaskServiceImpl taskServiceImpl, ConfigurationRuleTypeDTO configurationSubPanelDTO) {
		rtsCategoryModel = new DefaultListModel();
		
		this.taskServiceImpl = taskServiceImpl;
		this.severities = configurationSubPanelDTO.getSeverities();
		this.language = configurationSubPanelDTO.getLanguage();
		this.ruletypes = configurationSubPanelDTO.getRuletypes();
		
		initComponents();
		loadAfterChange();
		loadRuleTypeCategories();
	}

    private void initComponents() {
		
		categoryScrollpane = new JScrollPane();
		category = new JList();
		ruletypeScrollpane = new JScrollPane();
		ruletypeTable = new JTable();
		apply = new JButton();
		restore = new JButton();
		restoreAll = new JButton();
		
		category.setModel(rtsCategoryModel);
		category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categoryScrollpane.setViewportView(category);

		ruletypeTable.setFillsViewportHeight(true);
		ruletypeTable.getTableHeader().setReorderingAllowed(false);
		ruletypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ruletypeScrollpane.setViewportView(ruletypeTable);
		
		restore.setEnabled(false);
		
		category.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				 if (evt.getValueIsAdjusting()) {
					 return;
				 } 
				CategoryValueChanged();
			}
		});
		
		ruletypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				checkRestoreButtonEnabled();
			}
		});
		
		apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if(ruletypeTable.getSelectedRow() > -1){
					ApplyActionPerformed();
				} else{
					ServiceProvider.getInstance().getControlService().showInfoMessage((ServiceProvider.getInstance().getControlService().getTranslatedString("RowNotSelected")));
				}
			}
		});
		
		restore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				RestoreActionPerformed();
			}
		});
		
		restoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				RestoreAllActionPerformed();
			}
		});

		createLayout();
    }
	
	private void createLayout(){
		GroupLayout ruletypeSeverityLayout = new GroupLayout(this);
		
		GroupLayout.ParallelGroup horizontalButtonGroup = ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false);
		horizontalButtonGroup.addComponent(restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(restoreAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(apply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		
		GroupLayout.SequentialGroup horizontalPaneGroup = ruletypeSeverityLayout.createSequentialGroup();
		horizontalPaneGroup.addComponent(categoryScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addComponent(ruletypeScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPaneGroup.addGroup(horizontalButtonGroup);
		horizontalPaneGroup.addContainerGap();
		ruletypeSeverityLayout.setHorizontalGroup(horizontalPaneGroup);
		
		GroupLayout.SequentialGroup verticalButtonGroup = ruletypeSeverityLayout.createSequentialGroup();
		verticalButtonGroup.addContainerGap();
		verticalButtonGroup.addComponent(restore);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(restoreAll);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(apply);
		verticalButtonGroup.addContainerGap();
		
		GroupLayout.ParallelGroup verticalPaneGroup = ruletypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		verticalPaneGroup.addComponent(categoryScrollpane);
		verticalPaneGroup.addComponent(ruletypeScrollpane);
		verticalPaneGroup.addGroup(verticalButtonGroup);
		
		ruletypeSeverityLayout.setVerticalGroup(verticalPaneGroup);
		
		setLayout(ruletypeSeverityLayout);
	}
	
	public final void loadAfterChange(){
		setText();
		loadModel();
	}
	
	private void setText(){
		category.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Category")));
		apply.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Apply"));
		restore.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("RestoreToDefault"));
		restoreAll.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("RestoreAllToDefault"));
	}
	
	public void setSeverities(List<Severity> severities){
		this.severities = severities;
	}
	
	private void loadModel(){
		String[] ruletypeColumnNames = {ServiceProvider.getInstance().getControlService().getTranslatedString("Ruletype"), ServiceProvider.getInstance().getControlService().getTranslatedString("Severity")};
		ruletypeModel = new ComboBoxTableModel(ruletypeColumnNames, 0, severities);
		ruletypeModel.setTypes(new Class[]{DataLanguageHelper.class, Severity.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});
		
		ruletypeTable.setModel(ruletypeModel);
		
		TableColumnModel tcm = ruletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());
	}
	
	private void RestoreActionPerformed() {		
		taskServiceImpl.restoreKeyToDefaultSeverity(language, ((DataLanguageHelper) ruletypeModel.getValueAt(ruletypeTable.getSelectedRow(), 0)).key);
		CategoryValueChanged();
	}

	private void RestoreAllActionPerformed() {
		taskServiceImpl.restoreAllKeysToDefaultSeverities(language);
		CategoryValueChanged();
	}

	private void ApplyActionPerformed() {
		checkRestoreButtonEnabled();
		updateRuletypeSeverities();
	}
	
	
	private void CategoryValueChanged() {
		loadRuleTypes(((DataLanguageHelper) category.getSelectedValue()).key);
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
		if(ruletypeTable.getSelectedRow() > -1){
			restore.setEnabled(true);
		} else{
			restore.setEnabled(false);
		}
	}
}
