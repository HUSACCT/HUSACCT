package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.presentation.LanguageSeverityConfiguration;
import husacct.validate.presentation.tableModels.ComboBoxTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import org.apache.log4j.Logger;

public class ViolationTypeSeverityPanel extends javax.swing.JPanel {
	
	private static Logger logger = Logger.getLogger(ViolationTypeSeverityPanel.class);
	
	private ComboBoxTableModel violationtypeModel;
	private TaskServiceImpl taskServiceImpl;
	
	private JButton Apply, Restore, RestoreAll;
	private JList Category;
	private JScrollPane CategoryScrollpane, ViolationtypeScrollpane;
	private JTable ViolationtypeTable;
	
	private final DefaultListModel CategoryModel;
	private final String language;
	private final Map<String, List<ViolationType>> violationTypes;
	private final LanguageSeverityConfiguration languageSeverityConfiguration;
	
	public ViolationTypeSeverityPanel(TaskServiceImpl taskServiceImpl, LanguageSeverityConfiguration languageSeverityConfiguration, Map<String, List<ViolationType>> violationtypes, String language) {
		
		CategoryModel = new DefaultListModel();
		
		this.taskServiceImpl = taskServiceImpl;
		this.languageSeverityConfiguration = languageSeverityConfiguration;
		this.language = language;
		this.violationTypes = violationtypes;
		
		initComponents();
		setText();
	}
    
    private void initComponents() {
		
		CategoryScrollpane = new JScrollPane();
		Category = new JList();
		ViolationtypeScrollpane = new JScrollPane();
		ViolationtypeTable = new JTable();
		Restore = new JButton();
		RestoreAll = new JButton();
		Apply = new JButton();
		
		Category.setModel(CategoryModel);
		Category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Category.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				vtsCategoryValueChanged();
			}
		});
		CategoryScrollpane.setViewportView(Category);
		
		ViolationtypeTable.setFillsViewportHeight(true);
		ViolationtypeTable.getTableHeader().setReorderingAllowed(false);
		ViolationtypeTable.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		ViolationtypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				checkRestoreButtonEnabled();
			}
		});
		
		ViolationtypeScrollpane.setViewportView(ViolationtypeTable);
		
		Restore.setEnabled(false);
		Restore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsRestoreActionPerformed();
			}
		});
		
		RestoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsRestoreAllActionPerformed();
			}
		});
		
		Apply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsApplyActionPerformed();
			}
		});

		GroupLayout violationtypeSeverityLayout = new GroupLayout(this);
		violationtypeSeverityLayout.setHorizontalGroup(
			violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(violationtypeSeverityLayout.createSequentialGroup()
					.addComponent(CategoryScrollpane)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(ViolationtypeScrollpane)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addComponent(Restore)
						.addComponent(RestoreAll)
						.addComponent(Apply)
					)
					.addContainerGap()
				)
		);
		violationtypeSeverityLayout.setVerticalGroup(
			violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(CategoryScrollpane)
				.addComponent(ViolationtypeScrollpane)
				.addGroup(violationtypeSeverityLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(Restore)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(RestoreAll)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(Apply)
					.addContainerGap())
		);
		
		this.setLayout(violationtypeSeverityLayout);
    }
	
	public void setText(){
		Category.setBorder(BorderFactory.createTitledBorder(ValidateTranslator.getValue("Category")));
		Restore.setText(ValidateTranslator.getValue("RestoreToDefault"));
		RestoreAll.setText(ValidateTranslator.getValue("RestoreAllToDefault"));
		Apply.setText(ValidateTranslator.getValue("Apply"));
		
		loadModel();
	}
    
	public void loadModel(){
		String[] violationtypeModelHeaders = {ValidateTranslator.getValue("Violationtype"), ValidateTranslator.getValue("Severity")};
		violationtypeModel = new ComboBoxTableModel(violationtypeModelHeaders, 0, languageSeverityConfiguration.getSeverityNames());
		violationtypeModel.setTypes(new Class[]{String.class, Severity.class});
		violationtypeModel.setCanEdit(new Boolean[]{false, true});
		
		ViolationtypeTable.setModel(violationtypeModel);
		
		TableColumnModel tcm2 = ViolationtypeTable.getColumnModel();
		tcm2.getColumn(1).setCellEditor(violationtypeModel.getEditor());
		
		loadViolationTypeCategories();
	}
	
	private void vtsRestoreActionPerformed() {		
		taskServiceImpl.restoreToDefault(language, ValidateTranslator.getKey((String) violationtypeModel.getValueAt(ViolationtypeTable.getSelectedRow(), 0)));
		vtsCategoryValueChanged();
	}

	private void vtsRestoreAllActionPerformed() {
		taskServiceImpl.restoreAllToDefault(language);
		vtsCategoryValueChanged();
	}

	private void vtsApplyActionPerformed() {
		checkRestoreButtonEnabled();
		updateViolationtypeSeverities();
	}
	
	private void vtsCategoryValueChanged() {
		checkRestoreButtonEnabled();
		loadViolationType((String) Category.getSelectedValue());
	}
	
	private void updateViolationtypeSeverities() {
		HashMap<String, Severity> map = new HashMap<String, Severity>();

		for(int i = 0; i < violationtypeModel.getRowCount(); i++){
			map.put(ValidateTranslator.getKey((String) violationtypeModel.getValueAt(i, 0)), (Severity) violationtypeModel.getValueAt(i, 1));
		}

		taskServiceImpl.updateSeverityPerType(map, language);
	}
	
	private void loadViolationTypeCategories() {
		CategoryModel.clear();
		for (String categoryString : violationTypes.keySet()) {
			CategoryModel.addElement(ValidateTranslator.getValue(categoryString));
		}

	}

	private void loadViolationType(String category) {
		violationtypeModel.clear();
		for (String categoryString : violationTypes.keySet()) {
			if (ValidateTranslator.getValue(categoryString).equals(category)){
				List<ViolationType> violationtypes = violationTypes.get(categoryString);
				for(ViolationType violationtype: violationtypes){
					Severity severity;
					try{
						severity = taskServiceImpl.getSeverityFromKey(language, violationtype.getViolationtypeKey());
					} catch (Exception e){
						logger.error(e);
						severity = taskServiceImpl.getAllSeverities().get(0);
					}
					violationtypeModel.addRow(new Object[]{ValidateTranslator.getValue(violationtype.getViolationtypeKey()), severity});
				}
			}

		}
		violationtypeModel.checkValuesAreValid();
	}
	
	private void checkRestoreButtonEnabled(){
		if(ViolationtypeTable.getSelectedRow() > -1){
			Restore.setEnabled(true);
		} else{
			Restore.setEnabled(false);
		}
	}
}
