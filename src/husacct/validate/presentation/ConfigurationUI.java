package husacct.validate.presentation;

import husacct.validate.domain.validation.Severity;
import husacct.validate.presentation.TableModels.ColorChooserEditor;
import husacct.validate.presentation.TableModels.ColorTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class ConfigurationUI extends javax.swing.JInternalFrame {

	private static final long serialVersionUID = 3568220674416621458L;
	TaskServiceImpl ts;
	ColorTableModel severityModel;

	public ConfigurationUI(TaskServiceImpl ts) {
		this.ts = ts;
		severityModel = new ColorTableModel();
		initComponents();
		TableColumn column = severityNameTable.getColumnModel().getColumn(1);

		TableCellEditor editor = new ColorChooserEditor(new JButton());
		column.setCellEditor(editor);
		loadLanguageTabs();
		loadSeverity();
	}

    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        severityNamePanel = new javax.swing.JPanel();
        severityNameScrollPane = new javax.swing.JScrollPane();
        severityNameTable = new javax.swing.JTable();
        add = new javax.swing.JButton();
        remove = new javax.swing.JButton();
        up = new javax.swing.JButton();
        down = new javax.swing.JButton();
        applySeverity = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Configuration");
        setVisible(true);

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

        applySeverity.setText("Apply");
        applySeverity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applySeverityActionPerformed(evt);
            }
        });

        jButton1.setText("<html>Resore to<br> default");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout severityNamePanelLayout = new javax.swing.GroupLayout(severityNamePanel);
        severityNamePanel.setLayout(severityNamePanelLayout);
        severityNamePanelLayout.setHorizontalGroup(
            severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(severityNamePanelLayout.createSequentialGroup()
                .addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(remove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(up, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(applySeverity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(down, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        severityNamePanelLayout.setVerticalGroup(
            severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
            .addGroup(severityNamePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(up)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(down)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applySeverity)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Severity configuration", severityNamePanel);

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancel)
                .addGap(16, 16, 16))
        );
    }

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

	private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed
		if (severityNameTable.getSelectedRow() > 0) {
			severityModel.moveRow(severityNameTable.getSelectedRow(),
								  severityNameTable.getSelectedRow(),
								  severityNameTable.getSelectedRow() - 1);
			severityNameTable.changeSelection(severityNameTable.getSelectedRow() -
											  1, 0, false, false);
		}
	}//GEN-LAST:event_upActionPerformed

	private void removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeActionPerformed
		if (severityNameTable.getRowCount() > 1 && severityNameTable.getSelectedRow() > -1) {
			severityModel.removeRow(severityNameTable.getSelectedRow());
		}
	}//GEN-LAST:event_removeActionPerformed

	private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
		severityModel.insertRow(0, new Object[]{"", Color.BLACK});
		severityNameTable.changeSelection(0, 0,
										  false, false);
	}//GEN-LAST:event_addActionPerformed

	private void applySeverityActionPerformed(java.awt.event.ActionEvent evt) {
		LinkedHashMap<Integer, String> linkedmap = new LinkedHashMap<Integer, String>();

		for(int i = 0; i < severityModel.getRowCount(); i++){
			linkedmap.put(i, (String) severityModel.getValueAt(i, 0));
			linkedmap.put(i, (String) severityModel.getValueAt(i, 1));
			linkedmap.put(i, (String) severityModel.getValueAt(i, 2));
		}

		ts.applySeverities(linkedmap);
		loadSeverity();
		removeLanguageTabs();
		loadLanguageTabs();
	}

	private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

	}

    private javax.swing.JButton add;
    private javax.swing.JButton applySeverity;
    private javax.swing.JButton cancel;
    private javax.swing.JButton down;
    private javax.swing.JButton jButton1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton remove;
    private javax.swing.JPanel severityNamePanel;
    private javax.swing.JScrollPane severityNameScrollPane;
    private javax.swing.JTable severityNameTable;
    private javax.swing.JButton up;

	private void loadSeverity() {
		clearModel(severityModel);
		List<Severity> severities = ts.getAllSeverities();
		for (Severity severity : severities) {
			severityModel.addRow(new Object[]{severity.toString(),
											  severity.getColor()});
		}

	}

	private void clearModel(ColorTableModel model) {
		while (0 < model.getRowCount()) {
			model.removeRow(0);
		}
	}

	private void loadLanguageTabs(){
		for (String language : ts.getAvailableLanguages()) {
			LanguageSeverityConfiguration lcp = new LanguageSeverityConfiguration(
					language, ts.getRuletypes(language), ts.getAllSeverities(), ts);
			jTabbedPane1.addTab(language, lcp);
		}
	}

	private void removeLanguageTabs(){
		while(jTabbedPane1.getTabCount() > 1){
			jTabbedPane1.remove(1);
		}
	}
}
