package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.presentation.DataLanguageHelper;
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
	
	private static final long serialVersionUID = 1283848062887016417L;

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
		loadModel();
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

		createLayout();
		loadViolationTypeCategories();
    }
	
	private void createLayout(){
		GroupLayout violationtypeSeverityLayout = new GroupLayout(this);
		
		GroupLayout.ParallelGroup horizontalButtonGroup = violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false);
		horizontalButtonGroup.addComponent(Restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(RestoreAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(Apply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		
		GroupLayout.SequentialGroup horizontalPaneGroup = violationtypeSeverityLayout.createSequentialGroup();
		horizontalPaneGroup.addComponent(CategoryScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		horizontalPaneGroup.addComponent(ViolationtypeScrollpane);
		horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		horizontalPaneGroup.addGroup(horizontalButtonGroup);
		horizontalPaneGroup.addContainerGap();
		
		violationtypeSeverityLayout.setHorizontalGroup(horizontalPaneGroup);
		
		GroupLayout.SequentialGroup verticalButtonGroup = violationtypeSeverityLayout.createSequentialGroup();
		verticalButtonGroup.addContainerGap();
		verticalButtonGroup.addComponent(Restore);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(RestoreAll);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(Apply);
		verticalButtonGroup.addContainerGap();
		
		GroupLayout.ParallelGroup verticalPaneGroup = violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		verticalPaneGroup.addComponent(CategoryScrollpane);
		verticalPaneGroup.addComponent(ViolationtypeScrollpane);
		verticalPaneGroup.addGroup(verticalButtonGroup);
		
		violationtypeSeverityLayout.setVerticalGroup(verticalPaneGroup);
		
		setLayout(violationtypeSeverityLayout);
	}
	
	public void loadAfterChange(){
		setText();
		loadModel();
		loadViolationTypeCategories();
	}
	
	private void setText(){
		Category.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Category")));
		Restore.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("RestoreToDefault"));
		RestoreAll.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("RestoreAllToDefault"));
		Apply.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Apply"));
	}
    
	private void loadModel(){
		String[] violationtypeModelHeaders = {ServiceProvider.getInstance().getControlService().getTranslatedString("Violationtype"), ServiceProvider.getInstance().getControlService().getTranslatedString("Severity")};
		violationtypeModel = new ComboBoxTableModel(violationtypeModelHeaders, 0, languageSeverityConfiguration.getSeverityNames());
		violationtypeModel.setTypes(new Class[]{String.class, Severity.class});
		violationtypeModel.setCanEdit(new Boolean[]{false, true});
		
		ViolationtypeTable.setModel(violationtypeModel);
		
		TableColumnModel tcm2 = ViolationtypeTable.getColumnModel();
		tcm2.getColumn(1).setCellEditor(violationtypeModel.getEditor());
	}
	
	private void vtsRestoreActionPerformed() {		
		taskServiceImpl.restoreToDefault(language, ((DataLanguageHelper) violationtypeModel.getValueAt(ViolationtypeTable.getSelectedRow(), 0)).key);
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
		loadViolationType(((DataLanguageHelper) Category.getSelectedValue()).key);
	}
	
	private void updateViolationtypeSeverities() {
		HashMap<String, Severity> map = new HashMap<String, Severity>();

		for(int i = 0; i < violationtypeModel.getRowCount(); i++){
			String key = ((DataLanguageHelper) violationtypeModel.getValueAt(i, 0)).key;
			map.put(key, (Severity) violationtypeModel.getValueAt(i, 1));
			
		}

		taskServiceImpl.updateSeverityPerType(map, language);
	}
	
	private void loadViolationTypeCategories() {
		CategoryModel.clear();
		System.out.println(violationTypes.keySet().size());
		for (String categoryString : violationTypes.keySet()) {
			CategoryModel.addElement(new DataLanguageHelper(categoryString));
		}

	}

	private void loadViolationType(String category) {
		violationtypeModel.clear();
		for (String categoryString : violationTypes.keySet()) {
			if (categoryString.equals(category)){
				List<ViolationType> violationtypes = violationTypes.get(categoryString);
				for(ViolationType violationtype: violationtypes){
					Severity severity;
					try{
						severity = taskServiceImpl.getSeverityFromKey(language, violationtype.getViolationtypeKey());
					} catch (Exception e){
						logger.error(e);
						severity = taskServiceImpl.getAllSeverities().get(0);
					}
					violationtypeModel.addRow(new Object[]{new DataLanguageHelper(violationtype.getViolationtypeKey()), severity});
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
