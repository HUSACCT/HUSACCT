package husacct.validate.presentation;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.TableModels.ComboBoxTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class LanguageSeverityConfiguration extends JPanel {

	private final ComboBoxTableModel ruletypeModel;
	private final ComboBoxTableModel violationtypeModel;
	private final DefaultTableModel avtViolationtypeModel;
	private final DefaultListModel rtsCategoryModel;
	private final DefaultListModel vtsCategoryModel;
	private final DefaultListModel avtCategoryModel;
	private final DefaultListModel avtRuletypeModel;
	private final String language;
	private final HashMap<String, List<RuleType>> ruletypes;
	private final Map<String, List<ViolationType>> violationTypes;
	private final TaskServiceImpl ts;

	private JPanel activeViolationtype, ruletypeSeverity, violationtypeSeverity;
	private JButton avtApply, avtDeselectAll, avtSelectAll, rtsApply, rtsRestore,
			rtsRestoreAll, vtsApply, vtsRestore, vtsRestoreAll;
	private JList avtCategory, avtViolationtypeTable, rtsCategory, vtsCategory;
	private JScrollPane avtCategoryScrollpane, avtRuletypeScrollpane,
			avtViolationtypeScrollpane, rtsCategoryScrollpane, rtsRuletypeScrollpane,
			vtsCategoryScrollpane, vtsViolationtypeScrollpane;
	private JTable avtRuletype, rtsRuletypeTable, vtsViolationtypeTable;
	private JTabbedPane tabbedPane;

	public LanguageSeverityConfiguration(String language, Map<String, List<ViolationType>> violationTypes,
			HashMap<String, List<RuleType>> ruletypes, List<Severity> severityNames,
			TaskServiceImpl ts) {
		this.language = language;
		this.ruletypes = ruletypes;
		this.ts = ts;
		this.violationTypes = violationTypes;
		String[] ruletypeColumnNames = {ResourceBundles.getValue("Ruletype"), ResourceBundles.getValue("Severity")};
		ruletypeModel = new ComboBoxTableModel(ruletypeColumnNames, 0,
											   severityNames);
		ruletypeModel.setTypes(new Class[]{String.class, Severity.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});
		
		
		String[] violationtypeModelHeaders = {ResourceBundles.getValue("Violationtype"), ResourceBundles.getValue("Severity")};
		violationtypeModel = new ComboBoxTableModel(violationtypeModelHeaders, 0, severityNames);
		violationtypeModel.setTypes(new Class[]{String.class, Severity.class});
		violationtypeModel.setCanEdit(new Boolean[]{false, true});

		String[] avtViolationtypeModelHeaders = {ResourceBundles.getValue("Violationtype"), ResourceBundles.getValue("Active")};
		avtViolationtypeModel = new DefaultTableModel(avtViolationtypeModelHeaders, 0);

		rtsCategoryModel = new DefaultListModel();
		vtsCategoryModel = new DefaultListModel();
		avtCategoryModel = new DefaultListModel();
		avtRuletypeModel = new DefaultListModel();

		initComponents();
		
		TableColumnModel tcm = rtsRuletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());

		TableColumnModel tcm2 = vtsViolationtypeTable.getColumnModel();
		tcm2.getColumn(1).setCellEditor(violationtypeModel.getEditor());
		initializeEverything();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		
		tabbedPane = new JTabbedPane();
		ruletypeSeverity = new JPanel();
		rtsCategoryScrollpane = new JScrollPane();
		rtsCategory = new JList();
		rtsRuletypeScrollpane = new JScrollPane();
		rtsRuletypeTable = new JTable();
		rtsApply = new JButton();
		rtsRestore = new JButton();
		rtsRestoreAll = new JButton();
		violationtypeSeverity = new JPanel();
		vtsCategoryScrollpane = new JScrollPane();
		vtsCategory = new JList();
		vtsViolationtypeScrollpane = new JScrollPane();
		vtsViolationtypeTable = new JTable();
		vtsRestore = new JButton();
		vtsRestoreAll = new JButton();
		vtsApply = new JButton();
		activeViolationtype = new JPanel();
		avtCategoryScrollpane = new JScrollPane();
		avtCategory = new JList();
		avtRuletypeScrollpane = new JScrollPane();
		avtRuletype = new JTable();
		avtViolationtypeScrollpane = new JScrollPane();
		avtViolationtypeTable = new JList();
		avtSelectAll = new JButton();
		avtDeselectAll = new JButton();
		avtApply = new JButton();
		
		rtsCategory.setBorder(BorderFactory.createTitledBorder(ResourceBundles.getValue("Category")));
		rtsCategory.setModel(rtsCategoryModel);
		rtsCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rtsCategory.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				 if (evt.getValueIsAdjusting()) {
					 return;
				 } 
				rtsCategoryValueChanged();
			}
		});
		rtsCategoryScrollpane.setViewportView(rtsCategory);

		rtsRuletypeTable.setModel(ruletypeModel);
		rtsRuletypeTable.setFillsViewportHeight(true);
		rtsRuletypeTable.getTableHeader().setReorderingAllowed(false);
		rtsRuletypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rtsRuletypeScrollpane.setViewportView(rtsRuletypeTable);

		rtsApply.setText(ResourceBundles.getValue("Apply"));
		rtsApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsApplyActionPerformed();
			}
		});

		rtsRestore.setText(ResourceBundles.getValue("RestoreToDefault"));
		rtsRestore.setActionCommand("Restore to default");
		rtsRestore.setEnabled(false);
		rtsRestore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsRestoreActionPerformed();
			}
		});

		rtsRestoreAll.setText(ResourceBundles.getValue("RestoreAllToDefault"));
		rtsRestoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsRestoreAllActionPerformed();
			}
		});

		GroupLayout ruletypeSeverityLayout = new GroupLayout(ruletypeSeverity);
		ruletypeSeverityLayout.setHorizontalGroup(
			ruletypeSeverityLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(ruletypeSeverityLayout.createSequentialGroup()
					.addComponent(rtsCategoryScrollpane, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rtsRuletypeScrollpane, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(ruletypeSeverityLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(ruletypeSeverityLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(rtsRestore, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addComponent(rtsRestoreAll, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addComponent(rtsApply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		ruletypeSeverityLayout.setVerticalGroup(
			ruletypeSeverityLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(rtsCategoryScrollpane)
				.addComponent(rtsRuletypeScrollpane, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
				.addGroup(ruletypeSeverityLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(rtsRestore, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rtsRestoreAll, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
					.addComponent(rtsApply)
					.addContainerGap())
		);
		ruletypeSeverity.setLayout(ruletypeSeverityLayout);

		tabbedPane.addTab(ResourceBundles.getValue("SetRuletypeSeverity"), ruletypeSeverity);

		vtsCategory.setBorder(BorderFactory.createTitledBorder(ResourceBundles.getValue("Category")));
		vtsCategory.setModel(vtsCategoryModel);
		vtsCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		vtsCategory.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				vtsCategoryValueChanged();
			}
		});
		vtsCategoryScrollpane.setViewportView(vtsCategory);

		vtsViolationtypeTable.setModel(violationtypeModel);
		vtsViolationtypeTable.setFillsViewportHeight(true);
		vtsViolationtypeTable.getTableHeader().setReorderingAllowed(false);
		vtsViolationtypeTable.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		vtsViolationtypeScrollpane.setViewportView(vtsViolationtypeTable);

		vtsRestore.setText(ResourceBundles.getValue("RestoreToDefault"));
		vtsRestore.setEnabled(false);
		vtsRestore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsRestoreActionPerformed();
			}
		});

		vtsRestoreAll.setText(ResourceBundles.getValue("RestoreAllToDefault"));
		vtsRestoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsRestoreAllActionPerformed();
			}
		});

		vtsApply.setText(ResourceBundles.getValue("Apply"));
		vtsApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsApplyActionPerformed();
			}
		});

		GroupLayout violationtypeSeverityLayout = new GroupLayout(
				violationtypeSeverity);
		violationtypeSeverityLayout.setHorizontalGroup(
			violationtypeSeverityLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(violationtypeSeverityLayout.createSequentialGroup()
					.addComponent(vtsCategoryScrollpane, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(vtsViolationtypeScrollpane, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(violationtypeSeverityLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(violationtypeSeverityLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(vtsRestore, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addComponent(vtsRestoreAll, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addComponent(vtsApply, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		violationtypeSeverityLayout.setVerticalGroup(
			violationtypeSeverityLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(vtsCategoryScrollpane)
				.addComponent(vtsViolationtypeScrollpane, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
				.addGroup(violationtypeSeverityLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(vtsRestore, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(vtsRestoreAll, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
					.addComponent(vtsApply)
					.addContainerGap())
		);
		violationtypeSeverity.setLayout(violationtypeSeverityLayout);

		tabbedPane.addTab(ResourceBundles.getValue("SetViolationSeverity"), violationtypeSeverity);

		avtCategory.setBorder(BorderFactory.createTitledBorder(ResourceBundles.getValue("Category")));
		avtCategory.setModel(avtCategoryModel);
		avtCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		avtCategory.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				avtCategoryValueChanged();
			}
		});
		avtCategoryScrollpane.setViewportView(avtCategory);

		avtRuletype.setModel(avtViolationtypeModel);
		avtRuletype.setFillsViewportHeight(true);
		avtRuletype.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		avtRuletypeScrollpane.setViewportView(avtRuletype);

		avtViolationtypeTable.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Ruletypes")));
		avtViolationtypeTable.setModel(avtRuletypeModel);
		vtsViolationtypeTable.getTableHeader().setReorderingAllowed(false);
		avtViolationtypeTable.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		avtViolationtypeTable.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				avtViolationtypeTableValueChanged();
			}
		});
		avtViolationtypeScrollpane.setViewportView(avtViolationtypeTable);

		avtSelectAll.setText(ResourceBundles.getValue("SelectAll"));
		avtSelectAll.setActionCommand(ResourceBundles.getValue("Restore to default"));
		avtSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtSelectAllActionPerformed();
			}
		});

		avtDeselectAll.setText(ResourceBundles.getValue("DeselectAll"));
		avtDeselectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtDeselectAllActionPerformed();
			}
		});

		avtApply.setText(ResourceBundles.getValue("Apply"));
		avtApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtApplyActionPerformed();
			}
		});

		GroupLayout activeViolationtypeLayout = new GroupLayout(
				activeViolationtype);
		activeViolationtypeLayout.setHorizontalGroup(
			activeViolationtypeLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(activeViolationtypeLayout.createSequentialGroup()
					.addComponent(avtCategoryScrollpane, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(avtViolationtypeScrollpane, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(avtRuletypeScrollpane, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
					.addGap(6)
					.addGroup(activeViolationtypeLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(avtSelectAll, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
						.addGroup(activeViolationtypeLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(avtDeselectAll, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addComponent(avtApply, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		activeViolationtypeLayout.setVerticalGroup(
			activeViolationtypeLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(avtRuletypeScrollpane, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
				.addGroup(Alignment.LEADING, activeViolationtypeLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(avtSelectAll)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(avtDeselectAll)
					.addPreferredGap(ComponentPlacement.RELATED, 167, Short.MAX_VALUE)
					.addComponent(avtApply)
					.addContainerGap())
				.addComponent(avtCategoryScrollpane, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
				.addComponent(avtViolationtypeScrollpane, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
		);
		activeViolationtype.setLayout(activeViolationtypeLayout);

		tabbedPane.addTab(ResourceBundles.getValue("SetViolationtypeActivePerRuletype"),
						  activeViolationtype);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).
				addComponent(tabbedPane));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).
				addGroup(GroupLayout.Alignment.TRAILING, layout.
				createSequentialGroup().addContainerGap().addComponent(
				tabbedPane)));

//		tabbedPane.getAccessibleContext().setAccessibleName(
//				ResourceBundles.getValue("Set ruletype severity"));
	}
	
	public void initializeEverything() {
		loadRuleTypeCategories();
		loadViolationTypeCategories();
	}

	private void avtSelectAllActionPerformed() {
		
	}

	private void rtsRestoreActionPerformed() {
		
	}

	private void rtsRestoreAllActionPerformed() {
		
	}

	private void rtsApplyActionPerformed() {
		updateRuletypeSeverities();
	}

	private void vtsRestoreActionPerformed() {
		
	}

	private void vtsRestoreAllActionPerformed() {
		
	}

	private void vtsApplyActionPerformed() {
		updateViolationtypeSeverities();
	}

	private void avtDeselectAllActionPerformed() {
		
	}

	private void avtApplyActionPerformed() {
		
	}

	private void avtCategoryValueChanged() {
		
	}

	private void avtViolationtypeTableValueChanged() {
		
	}

	private void rtsCategoryValueChanged() {
		loadRuleTypes((String) rtsCategory.getSelectedValue());
	}

	private void vtsCategoryValueChanged() {
		loadViolationType((String) vtsCategory.getSelectedValue());
	}

	private void updateRuletypeSeverities() {
		HashMap<String, Severity> map = new HashMap<String, Severity>();

		for(int i = 0; i < ruletypeModel.getRowCount(); i++){
			map.put((String) ruletypeModel.getValueAt(i, 0), (Severity) ruletypeModel.getValueAt(i, 1));
		}

		ts.updateSeverityPerType(map, language);
	}
	
	private void updateViolationtypeSeverities() {
		HashMap<String, Severity> map = new HashMap<String, Severity>();

		for(int i = 0; i < violationtypeModel.getRowCount(); i++){
			map.put((String) violationtypeModel.getValueAt(i, 0), (Severity) violationtypeModel.getValueAt(i, 1));
		}

		ts.updateSeverityPerType(map, language);
	}

	private void loadRuleTypes(String category) {
		ruletypeModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			if (categoryString.equals(category)){
				List<RuleType> rules = ruletypes.get(category);
				for(RuleType ruletype: rules){
					Severity severity;
					try{
						severity = ts.getSeverityFromKey(language.toLowerCase(), ruletype.getKey());
					} catch (Exception e){
						severity = ts.getAllSeverities().get(0);
					}
					
					
					ruletypeModel.addRow(new Object[]{ruletype.getKey(), severity});
				}
			}

		}
		ruletypeModel.checkValuesAreValid();	
	}

	private void loadRuleTypeCategories() {
		rtsCategoryModel.clear();
		for (String categoryString : ruletypes.keySet()) {
			rtsCategoryModel.addElement(categoryString);
		}
	}

	private void loadViolationTypeCategories() {
		vtsCategoryModel.clear();
		for (String categoryString : violationTypes.keySet()) {
			vtsCategoryModel.addElement(categoryString);
		}

	}

	private void loadViolationType(String violationTypeKey) {
		violationtypeModel.clear();
		for (String categoryString : violationTypes.keySet()) {
			if (categoryString.equals(violationTypeKey)){
				List<ViolationType> violationtypes = violationTypes.get(violationTypeKey);
				for(ViolationType violationtype: violationtypes){
					Severity severity;
					try{
						severity = ts.getSeverityFromKey(language.toLowerCase(), violationtype.getViolationtypeKey());
					} catch (Exception e){
						severity = ts.getAllSeverities().get(0);
					}
					violationtypeModel.addRow(new Object[]{violationtype.getViolationtypeKey(), severity});
				}
			}

		}
		violationtypeModel.checkValuesAreValid();
	}
}
