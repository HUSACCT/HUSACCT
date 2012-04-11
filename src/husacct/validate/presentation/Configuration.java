package husacct.validate.presentation;

import javax.swing.table.DefaultTableModel;

public class Configuration extends javax.swing.JInternalFrame {

    DefaultTableModel ruleTypeModel;
    DefaultTableModel vtViolationModel;

    public Configuration() {
	String[] ruleTypecolumnNames = {"Ruletype", "Severity", "Active"};
	ruleTypeModel = new DefaultTableModel(ruleTypecolumnNames, 0){
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

	    @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

	    @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
	String[] violationTypeColumnNames = {"Violationtype", "Severity", "Active"};
	vtViolationModel= new DefaultTableModel(violationTypeColumnNames, 0){
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

	    @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

	    @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
	initComponents();
	LoadRuleTypes();
	LoadViolationTypes();
    }

    @SuppressWarnings("unchecked")
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
        severityNamePanel = new javax.swing.JScrollPane();
        severityNameTable = new javax.swing.JTable();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        ruleTypeTable.setAutoCreateRowSorter(true);
        ruleTypeTable.setModel(ruleTypeModel);
        ruleTypeTable.setAutoscrolls(false);
        ruleTypeTable.setFillsViewportHeight(true);
        ruleTypeTable.getTableHeader().setReorderingAllowed(false);
        ruleTypePanel.setViewportView(ruleTypeTable);

        jTabbedPane1.addTab("Rule Types", ruleTypePanel);

        vtRuleTypeList.setBorder(javax.swing.BorderFactory.createTitledBorder("Rule name"));
        vtRuleTypeList.setSelectedIndex(1);
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
            .addComponent(vtRuleTypesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
            .addComponent(vtViolationTypePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Violation Types", violationTypePanel);

        severityNameTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"High"},
                {"Medium"},
                {"Low"}
            },
            new String [] {
                "Severity Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        severityNameTable.setFillsViewportHeight(true);
        severityNamePanel.setViewportView(severityNameTable);

        jTabbedPane1.addTab("Severity Names", severityNamePanel);

        save.setText("Save");

        cancel.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(save)
                    .addComponent(cancel))
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane ruleTypePanel;
    private javax.swing.JTable ruleTypeTable;
    private javax.swing.JButton save;
    private javax.swing.JScrollPane severityNamePanel;
    private javax.swing.JTable severityNameTable;
    private javax.swing.JPanel violationTypePanel;
    private javax.swing.JList vtRuleTypeList;
    private javax.swing.JScrollPane vtRuleTypesPanel;
    private javax.swing.JScrollPane vtViolationTypePanel;
    private javax.swing.JTable vtViolationTypeTable;
    // End of variables declaration//GEN-END:variables

    private void LoadRuleTypes() {

    }

    private void LoadViolationTypes() {

    }
}
