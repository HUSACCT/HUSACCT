package husacct.validate.presentation;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.TaskServiceImpl;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class ConfigurationUI extends javax.swing.JInternalFrame {
	private static final long serialVersionUID = 3568220674416621458L;
	
	TaskServiceImpl ts;
    DefaultTableModel ruleTypeModel;
    DefaultTableModel vtViolationModel;
	DefaultTableModel severityModel;
	DefaultListModel vtRuleTypeModel = new DefaultListModel();

    public ConfigurationUI(TaskServiceImpl ts) {
		this.ts = ts;
	String[] ruleTypecolumnNames = {"Ruletype", "Severity", "Active"};
	ruleTypeModel = new DefaultTableModel(ruleTypecolumnNames, 0){		
		private static final long serialVersionUID = 464171433255568056L;
			Class<?>[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

	    @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

	    @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
	String[] violationTypeColumnNames = {"Violationtype", "Severity", "Active"};
	vtViolationModel= new DefaultTableModel(violationTypeColumnNames, 0){
		private static final long serialVersionUID = 9737194773040394L;
			Class<?>[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

	    @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

	    @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
	String[] severityColumnNames = {"Severity Name"};
	severityModel= new DefaultTableModel(severityColumnNames, 0){
		private static final long serialVersionUID = 6694887088914062519L;
			Class<?>[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true
            };

	    @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

	    @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
		initComponents();
		LoadRuleTypes();
		LoadViolationTypeRuleTypes();
		loadSeverity();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        ruleTypePanel = new javax.swing.JScrollPane();
        ruleTypeTable = new javax.swing.JTable();
        violationTypePanel = new javax.swing.JPanel();
        vtRuleTypesPanel = new javax.swing.JScrollPane();
        vtRuleTypeList = new javax.swing.JList();
        vtViolationTypePanel = new javax.swing.JScrollPane();
        vtViolationTypeTable = new javax.swing.JTable();
        severityNamePanel = new javax.swing.JPanel();
        severityNameScrollPane = new javax.swing.JScrollPane();
        severityNameTable = new javax.swing.JTable();
        add = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        up = new javax.swing.JButton();
        down = new javax.swing.JButton();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Configuration");
        setVisible(true);

        ruleTypeTable.setAutoCreateRowSorter(true);
        ruleTypeTable.setModel(ruleTypeModel);
        ruleTypeTable.setAutoscrolls(false);
        ruleTypeTable.setFillsViewportHeight(true);
        ruleTypeTable.getTableHeader().setReorderingAllowed(false);
        ruleTypePanel.setViewportView(ruleTypeTable);

        jTabbedPane1.addTab("Rule Types", ruleTypePanel);

        vtRuleTypeList.setBorder(javax.swing.BorderFactory.createTitledBorder("Rule name"));
        vtRuleTypeList.setModel(vtRuleTypeModel);
        vtRuleTypeList.setSelectedIndex(1);
        vtRuleTypeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                valueChangedRuleTypeList(evt);
            }
        });
        vtRuleTypesPanel.setViewportView(vtRuleTypeList);

        vtViolationTypeTable.setAutoCreateRowSorter(true);
        vtViolationTypeTable.setModel(vtViolationModel);
        vtViolationTypeTable.setFillsViewportHeight(true);
        vtViolationTypeTable.getTableHeader().setReorderingAllowed(false);
        vtViolationTypePanel.setViewportView(vtViolationTypeTable);

        javax.swing.GroupLayout violationTypePanelLayout = new javax.swing.GroupLayout(violationTypePanel);
        violationTypePanel.setLayout(violationTypePanelLayout);
        violationTypePanelLayout.setHorizontalGroup(
            violationTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(violationTypePanelLayout.createSequentialGroup()
                .addComponent(vtRuleTypesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vtViolationTypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
        );
        violationTypePanelLayout.setVerticalGroup(
            violationTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vtRuleTypesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
            .addComponent(vtViolationTypePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Violation Types", violationTypePanel);

        severityNameTable.setModel(severityModel);
        severityNameTable.setFillsViewportHeight(true);
        severityNameScrollPane.setViewportView(severityNameTable);

        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        remove.setText("Remove");
        remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeActionPerformed(evt);
            }
        });

        up.setText("Up");
        up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upActionPerformed(evt);
            }
        });

        down.setText("Down");
        down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout severityNamePanelLayout = new javax.swing.GroupLayout(severityNamePanel);
        severityNamePanel.setLayout(severityNamePanelLayout);
        severityNamePanelLayout.setHorizontalGroup(
            severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(severityNamePanelLayout.createSequentialGroup()
                .addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(remove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(up, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(down, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        severityNamePanelLayout.setVerticalGroup(
            severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
            .addGroup(severityNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(up)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(down)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Severity configuration", severityNamePanel);

        save.setText("Save");

        cancel.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(330, 330, 330)
                .addComponent(save)
                .addGap(3, 3, 3)
                .addComponent(cancel)
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(save)
                    .addComponent(cancel))
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void valueChangedRuleTypeList(//GEN-FIRST:event_valueChangedRuleTypeList
			javax.swing.event.ListSelectionEvent evt) {//GEN-HEADEREND:event_valueChangedRuleTypeList
		String ruletype = (String) vtRuleTypeModel.getElementAt(vtRuleTypeList.
				getSelectedIndex());
		loadViolationType(ruletype);
	}//GEN-LAST:event_valueChangedRuleTypeList

	private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
		severityModel.removeRow(severityNameTable.getSelectedRow());
	}//GEN-LAST:event_removeActionPerformed

	private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
		severityModel.addRow(new Object[]{""});
		severityNameTable.changeSelection(severityNameTable.getRowCount() - 1, 0,
										  false, false);
	}//GEN-LAST:event_addActionPerformed

	private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed
		if (severityNameTable.getSelectedRow() > 0) {
			severityModel.moveRow(severityNameTable.getSelectedRow(),
								  severityNameTable.getSelectedRow(),
								  severityNameTable.getSelectedRow() - 1);
			severityNameTable.changeSelection(severityNameTable.getSelectedRow() -
											  1, 0, false, false);
		}
	}//GEN-LAST:event_upActionPerformed

	private void downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downActionPerformed
		if (severityNameTable.getSelectedRow() < severityNameTable.getRowCount() -
												 1) {
			severityModel.moveRow(severityNameTable.getSelectedRow(),
								  severityNameTable.getSelectedRow(),
								  severityNameTable.getSelectedRow() + 1);
			severityNameTable.changeSelection(severityNameTable.getSelectedRow() +
											  1, 0, false, false);
		}
	}//GEN-LAST:event_downActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton cancel;
    private javax.swing.JButton down;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton remove;
    private javax.swing.JScrollPane ruleTypePanel;
    private javax.swing.JTable ruleTypeTable;
    private javax.swing.JButton save;
    private javax.swing.JPanel severityNamePanel;
    private javax.swing.JScrollPane severityNameScrollPane;
    private javax.swing.JTable severityNameTable;
    private javax.swing.JButton up;
    private javax.swing.JPanel violationTypePanel;
    private javax.swing.JList vtRuleTypeList;
    private javax.swing.JScrollPane vtRuleTypesPanel;
    private javax.swing.JScrollPane vtViolationTypePanel;
    private javax.swing.JTable vtViolationTypeTable;
    // End of variables declaration//GEN-END:variables

	private void LoadRuleTypes() {
		List<RuleType> ruletypes = ts.getRuletypes();
		for (RuleType ruletype : ruletypes) {
			ruleTypeModel.addRow(new Object[]{ruletype.getKey(), 1, true});
		}
	}

	private void LoadViolationTypeRuleTypes() {
		List<RuleType> ruletypes = ts.getRuletypes();
		for (RuleType ruletype : ruletypes) {
			vtRuleTypeModel.add(vtRuleTypeModel.getSize(), ruletype.getKey());
		}
	}

	private void loadViolationType(String ruletypeKey) {
		System.out.println(ruletypeKey);
		List<RuleType> ruletypes = ts.getRuletypes();
		for (RuleType ruletype : ruletypes) {
			if (ruletype.getKey().equals(ruletypeKey)) {
				clearModel(vtViolationModel);
				for (ViolationType violationtype : ruletype.getViolationTypes()) {
					vtViolationModel.addRow(new Object[]{violationtype.
								getViolationtypeKey(), 1, true});
				}
			}
		}
	}

	private void clearModel(DefaultTableModel model) {
		int rows = vtViolationTypeTable.getRowCount();
		while (0 < rows) {
			model.removeRow(0);
			rows--;
		}
	}

	private void loadSeverity() {

		List<Severity> severities = ts.getAllSeverities();

		if(severities.isEmpty()){
			severityModel.addRow(new Object[]{"Low"});
			severityModel.addRow(new Object[]{"Meduim"});
			severityModel.addRow(new Object[]{"High"});
		} else{
			for(Severity severity : severities){
				severityModel.addRow(new Object[]{severity.getDefaultName()});
			}
		}
	}
}
