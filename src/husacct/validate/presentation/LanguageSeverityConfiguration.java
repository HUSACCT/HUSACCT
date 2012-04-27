package husacct.validate.presentation;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.TableModels.ComboBoxTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
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

	public LanguageSeverityConfiguration(String language,
			HashMap<String, List<RuleType>> ruletypes, List<Severity> severityNames,
			TaskServiceImpl ts) {
		this.language = language;
		this.ruletypes = ruletypes;
		this.ts = ts;
		String[] ruletypeColumnNames = {"Ruletype", "Severity"};
		ruletypeModel = new ComboBoxTableModel(ruletypeColumnNames, 0,
											   severityNames);
		ruletypeModel.setTypes(new Class[]{String.class, Severity.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});

		String[] violationtypeModelHeaders = {"Violationtype", "Severity"};
		violationtypeModel = new ComboBoxTableModel(violationtypeModelHeaders, 0, severityNames);
		violationtypeModel.setTypes(new Class[]{String.class, Severity.class});
		violationtypeModel.setCanEdit(new Boolean[]{false, true, true});

		String[] avtViolationtypeModelHeaders = {"Violationtype", "Active"};
		avtViolationtypeModel = new DefaultTableModel(avtViolationtypeModelHeaders, 0);
		violationtypeModel.setTypes(new Class[]{String.class, Boolean.class});
		violationtypeModel.setCanEdit(new Boolean[]{false, true, true});

		rtsCategoryModel = new DefaultListModel();
		vtsCategoryModel = new DefaultListModel();
		avtCategoryModel = new DefaultListModel();
		avtRuletypeModel = new DefaultListModel();

		initComponents();

		TableColumnModel tcm = rtsRuletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());
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

		rtsCategory.setBorder(BorderFactory.createTitledBorder("Category"));
		rtsCategory.setModel(rtsCategoryModel);
		rtsCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rtsCategory.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				rtsCategoryValueChanged();
			}
		});
		rtsCategoryScrollpane.setViewportView(rtsCategory);

		rtsRuletypeTable.setModel(ruletypeModel);
		rtsRuletypeTable.setFillsViewportHeight(true);
		rtsRuletypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rtsRuletypeScrollpane.setViewportView(rtsRuletypeTable);

		rtsApply.setText("Apply");
		rtsApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsApplyActionPerformed();
			}
		});

		rtsRestore.setText("<html>Restore<br> to default");
		rtsRestore.setActionCommand("Restore to default");
		rtsRestore.setEnabled(false);
		rtsRestore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsRestoreActionPerformed();
			}
		});

		rtsRestoreAll.setText("<html>Restore all<br> to default");
		rtsRestoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				rtsRestoreAllActionPerformed();
			}
		});

		GroupLayout ruletypeSeverityLayout = new GroupLayout(ruletypeSeverity);
		ruletypeSeverity.setLayout(ruletypeSeverityLayout);
		ruletypeSeverityLayout.setHorizontalGroup(
				ruletypeSeverityLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(ruletypeSeverityLayout.
				createSequentialGroup().addComponent(rtsCategoryScrollpane,
													 GroupLayout.DEFAULT_SIZE,
													 200, Short.MAX_VALUE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(rtsRuletypeScrollpane, GroupLayout.PREFERRED_SIZE,
							 406, GroupLayout.PREFERRED_SIZE).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED).addGroup(
				ruletypeSeverityLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING, false).addGroup(ruletypeSeverityLayout.
				createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				rtsRestore, GroupLayout.Alignment.TRAILING,
																				GroupLayout.PREFERRED_SIZE,
																				88,
																				GroupLayout.PREFERRED_SIZE).
				addComponent(rtsRestoreAll, GroupLayout.Alignment.TRAILING,
							 GroupLayout.PREFERRED_SIZE, 88,
							 GroupLayout.PREFERRED_SIZE)).addComponent(rtsApply,
																	   GroupLayout.DEFAULT_SIZE,
																	   GroupLayout.DEFAULT_SIZE,
																	   Short.MAX_VALUE)).
				addContainerGap()));
		ruletypeSeverityLayout.setVerticalGroup(
				ruletypeSeverityLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(
				rtsCategoryScrollpane).addComponent(rtsRuletypeScrollpane,
													GroupLayout.DEFAULT_SIZE,
													461, Short.MAX_VALUE).
				addGroup(GroupLayout.Alignment.TRAILING, ruletypeSeverityLayout.
				createSequentialGroup().addContainerGap().addComponent(
				rtsRestore, GroupLayout.PREFERRED_SIZE, 44,
																	   GroupLayout.PREFERRED_SIZE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(rtsRestoreAll, GroupLayout.PREFERRED_SIZE, 42,
							 GroupLayout.PREFERRED_SIZE).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
																		 Short.MAX_VALUE).
				addComponent(rtsApply).addContainerGap()));

		tabbedPane.addTab("Set ruletype severity", ruletypeSeverity);

		vtsCategory.setBorder(BorderFactory.createTitledBorder("Category"));
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
		vtsViolationtypeTable.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		vtsViolationtypeScrollpane.setViewportView(vtsViolationtypeTable);

		vtsRestore.setText("<html>Restore<br> to default");
		vtsRestore.setEnabled(false);
		vtsRestore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsRestoreActionPerformed();
			}
		});

		vtsRestoreAll.setText("<html>Restore all<br> to default");
		vtsRestoreAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsRestoreAllActionPerformed();
			}
		});

		vtsApply.setText("Apply");
		vtsApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				vtsApplyActionPerformed();
			}
		});

		GroupLayout violationtypeSeverityLayout = new GroupLayout(
				violationtypeSeverity);
		violationtypeSeverity.setLayout(violationtypeSeverityLayout);
		violationtypeSeverityLayout.setHorizontalGroup(
				violationtypeSeverityLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(violationtypeSeverityLayout.
				createSequentialGroup().addComponent(vtsCategoryScrollpane,
													 GroupLayout.DEFAULT_SIZE,
													 201, Short.MAX_VALUE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(vtsViolationtypeScrollpane,
							 GroupLayout.PREFERRED_SIZE, 405,
							 GroupLayout.PREFERRED_SIZE).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED).addGroup(
				violationtypeSeverityLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING, false).addGroup(violationtypeSeverityLayout.
				createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				vtsRestore, GroupLayout.Alignment.TRAILING,
																				GroupLayout.PREFERRED_SIZE,
																				88,
																				GroupLayout.PREFERRED_SIZE).
				addComponent(vtsRestoreAll, GroupLayout.Alignment.TRAILING,
							 GroupLayout.PREFERRED_SIZE, 88,
							 GroupLayout.PREFERRED_SIZE)).addComponent(vtsApply,
																	   GroupLayout.PREFERRED_SIZE,
																	   88,
																	   GroupLayout.PREFERRED_SIZE)).
				addContainerGap()));
		violationtypeSeverityLayout.setVerticalGroup(
				violationtypeSeverityLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(
				vtsCategoryScrollpane).addComponent(vtsViolationtypeScrollpane,
													GroupLayout.DEFAULT_SIZE,
													461, Short.MAX_VALUE).
				addGroup(GroupLayout.Alignment.TRAILING,
						 violationtypeSeverityLayout.createSequentialGroup().
				addContainerGap().addComponent(vtsRestore,
											   GroupLayout.PREFERRED_SIZE, 44,
											   GroupLayout.PREFERRED_SIZE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(vtsRestoreAll, GroupLayout.PREFERRED_SIZE, 42,
							 GroupLayout.PREFERRED_SIZE).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
																		 Short.MAX_VALUE).
				addComponent(vtsApply).addContainerGap()));

		tabbedPane.addTab("Set violation severity", violationtypeSeverity);

		avtCategory.setBorder(BorderFactory.createTitledBorder("Category"));
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
				"Ruletypes"));
		avtViolationtypeTable.setModel(avtRuletypeModel);
		avtViolationtypeTable.setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		avtViolationtypeTable.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				avtViolationtypeTableValueChanged();
			}
		});
		avtViolationtypeScrollpane.setViewportView(avtViolationtypeTable);

		avtSelectAll.setText("Select all");
		avtSelectAll.setActionCommand("Restore to default");
		avtSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtSelectAllActionPerformed();
			}
		});

		avtDeselectAll.setText("Deselect all");
		avtDeselectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtDeselectAllActionPerformed();
			}
		});

		avtApply.setText("Apply");
		avtApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				avtApplyActionPerformed();
			}
		});

		GroupLayout activeViolationtypeLayout = new GroupLayout(
				activeViolationtype);
		activeViolationtype.setLayout(activeViolationtypeLayout);
		activeViolationtypeLayout.setHorizontalGroup(
				activeViolationtypeLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(activeViolationtypeLayout.
				createSequentialGroup().addComponent(avtCategoryScrollpane,
													 GroupLayout.PREFERRED_SIZE,
													 153,
													 GroupLayout.PREFERRED_SIZE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(avtViolationtypeScrollpane,
							 GroupLayout.PREFERRED_SIZE, 156,
							 GroupLayout.PREFERRED_SIZE).addPreferredGap(
				LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
				avtRuletypeScrollpane, GroupLayout.PREFERRED_SIZE, 287,
																	   GroupLayout.PREFERRED_SIZE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
				activeViolationtypeLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING, false).addGroup(activeViolationtypeLayout.
				createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				avtSelectAll, GroupLayout.Alignment.TRAILING,
																				GroupLayout.PREFERRED_SIZE,
																				88,
																				GroupLayout.PREFERRED_SIZE).
				addComponent(avtDeselectAll, GroupLayout.Alignment.TRAILING,
							 GroupLayout.PREFERRED_SIZE, 88,
							 GroupLayout.PREFERRED_SIZE)).addComponent(avtApply,
																	   GroupLayout.PREFERRED_SIZE,
																	   88,
																	   GroupLayout.PREFERRED_SIZE)).
				addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		activeViolationtypeLayout.setVerticalGroup(
				activeViolationtypeLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(
				avtCategoryScrollpane).addComponent(avtRuletypeScrollpane,
													GroupLayout.DEFAULT_SIZE,
													461, Short.MAX_VALUE).
				addComponent(avtViolationtypeScrollpane).addGroup(
				GroupLayout.Alignment.TRAILING, activeViolationtypeLayout.
				createSequentialGroup().addContainerGap().addComponent(
				avtSelectAll).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED).addComponent(
				avtDeselectAll).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).addComponent(
				avtApply).addContainerGap()));

		tabbedPane.addTab("Set violationtype active per ruletype severit",
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

		tabbedPane.getAccessibleContext().setAccessibleName(
				"Set ruletype severity");
	}

	private void avtSelectAllActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_avtSelectAllActionPerformed

	private void rtsRestoreActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_rtsRestoreActionPerformed

	private void rtsRestoreAllActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_rtsRestoreAllActionPerformed

	private void rtsApplyActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_rtsApplyActionPerformed

	private void vtsRestoreActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_vtsRestoreActionPerformed

	private void vtsRestoreAllActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_vtsRestoreAllActionPerformed

	private void vtsApplyActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_vtsApplyActionPerformed

	private void avtDeselectAllActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_avtDeselectAllActionPerformed

	private void avtApplyActionPerformed() {
		// TODO add your handling code here:
	}//GEN-LAST:event_avtApplyActionPerformed

	private void avtCategoryValueChanged() {
		// TODO add your handling code here:
	}//GEN-LAST:event_avtCategoryValueChanged

	private void avtViolationtypeTableValueChanged() {
		// TODO add your handling code here:
	}//GEN-LAST:event_avtViolationtypeTableValueChanged

	private void rtsCategoryValueChanged() {
		// TODO add your handling code here:
	}//GEN-LAST:event_rtsCategoryValueChanged

	private void vtsCategoryValueChanged() {
		// TODO add your handling code here:
	}

	private void applyViolationTypesActionPerformed() {
		//TODO: Fix the fetching of the ruletypes en put them in a list to return to the reposetory
		ts.UpdateRuletype(ruletypeModel, violationtypeModel, language);
	}

	private void LoadRuleTypes(String category) {
		for (String categoryString : ruletypes.keySet()) {
			if (categoryString.equals(category)){
				List<RuleType> rules = ruletypes.get(category);
				for(RuleType ruletype: rules){
					ruletypeModel.addRow(new Object[]{ruletype.getKey(), ts.getAllSeverities().get(0)});
				}
			}

		}
	}

	private void loadViolationType(String ruletypeKey) {
//		for (RuleType ruletype : ruletypes) {
//			if (ruletype.getKey().equals(ruletypeKey)) {
//				clearModel(violationtypeModel);
//				for (ViolationType violationtype : ruletype.getViolationTypes()) {
//					violationtypeModel.addRow(new Object[]{violationtype.
//								getViolationtypeKey(), 1,
//														   violationtype.
//								isActive()});
//				}
//			}
//		}
	}

	private void clearModel(ComboBoxTableModel model) {
		int rows = model.getRowCount();
		while (0 < rows) {
			model.removeRow(0);
			rows--;
		}
	}
}
