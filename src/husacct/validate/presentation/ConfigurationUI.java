package husacct.validate.presentation;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.validation.Severity;
import husacct.validate.presentation.TableModels.ColorTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ConfigurationUI extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 3568220674416621458L;
    private TaskServiceImpl ts;
    private ColorTableModel severityModel;
    
    private JButton add, remove, down, up, cancel, applySeverity, restore;
    private JTabbedPane jTabbedPane1;
    private JPanel severityNamePanel;
    private JScrollPane severityNameScrollPane;
    private JTable severityNameTable;

    public ConfigurationUI(TaskServiceImpl ts) {
        this.ts = ts;
        severityModel = new ColorTableModel();
        initComponents();
        
        severityModel.setColorEditor(severityNameTable, 1);
        loadLanguageTabs();
        loadSeverity();
    }

    private void initComponents() {

        jTabbedPane1 = new JTabbedPane();
        severityNamePanel = new JPanel();
        severityNameScrollPane = new JScrollPane();
        severityNameTable = new JTable();
        add = new JButton();
        remove = new JButton();
        up = new JButton();
        down = new JButton();
        applySeverity = new JButton();
        restore = new JButton();
        cancel = new JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(ResourceBundles.getValue("Configuration"));

        severityNameTable.setModel(severityModel);
        severityNameTable.setFillsViewportHeight(true);
        severityNameScrollPane.setViewportView(severityNameTable);

        add.setText(ResourceBundles.getValue("Add"));
        add.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                addActionPerformed();
            }
        });

        remove.setText(ResourceBundles.getValue("Remove"));
        remove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                removeActionPerformed();
            }
        });

        up.setText(ResourceBundles.getValue("Up"));
        up.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                upActionPerformed();
            }
        });

        down.setText(ResourceBundles.getValue("Down"));
        down.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                downActionPerformed();
            }
        });

        applySeverity.setText(ResourceBundles.getValue("Apply"));
        applySeverity.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                applySeverityActionPerformed();
            }
        });

        restore.setText(ResourceBundles.getValue("<html>Resore to<br> default"));
        restore.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed();
            }
        });

        javax.swing.GroupLayout severityNamePanelLayout = new javax.swing.GroupLayout(severityNamePanel);
        severityNamePanel.setLayout(severityNamePanelLayout);
        severityNamePanelLayout.setHorizontalGroup(
                severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(severityNamePanelLayout.createSequentialGroup().addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(remove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(up, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(restore).addComponent(applySeverity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(down, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        severityNamePanelLayout.setVerticalGroup(
                severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE).addGroup(severityNamePanelLayout.createSequentialGroup().addContainerGap().addComponent(add).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(remove).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(up).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(down).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(restore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(applySeverity).addContainerGap()));

        jTabbedPane1.addTab(ResourceBundles.getValue("Severity configuration"), severityNamePanel);

        cancel.setText(ResourceBundles.getValue("Cancel"));
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                cancelActionPerformed();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(cancel).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jTabbedPane1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancel).addGap(16, 16, 16)));
    }

    private void downActionPerformed() {
        if (severityNameTable.getSelectedRow() < severityNameTable.getRowCount()
                - 1) {
            List<Severity> serverities = ts.getAllSeverities();
            Severity severity = serverities.get(severityNameTable.getSelectedRow());
            serverities.remove(severityNameTable.getSelectedRow());
            severityModel.moveRow(severityNameTable.getSelectedRow(),
                    severityNameTable.getSelectedRow(),
                    severityNameTable.getSelectedRow() + 1);
            severityNameTable.changeSelection(severityNameTable.getSelectedRow()
                    + 1, 0, false, false);
            serverities.add(severityNameTable.getSelectedRow(), severity);
            ts.addSeverities(serverities);
        }
    }

    private void upActionPerformed() {
        if (severityNameTable.getSelectedRow() > 0) {
            List<Severity> serverities = ts.getAllSeverities();
            Severity severity = serverities.get(severityNameTable.getSelectedRow());
            serverities.remove(severityNameTable.getSelectedRow());
            severityModel.moveRow(severityNameTable.getSelectedRow(),
                    severityNameTable.getSelectedRow(),
                    severityNameTable.getSelectedRow() - 1);
            severityNameTable.changeSelection(severityNameTable.getSelectedRow()
                    - 1, 0, false, false);
            serverities.add(severityNameTable.getSelectedRow(), severity);
            ts.addSeverities(serverities);
        }
    }

    private void removeActionPerformed() {
        if (severityNameTable.getRowCount() > 1 && severityNameTable.getSelectedRow() > -1) {
            severityModel.removeRow(severityNameTable.getSelectedRow());
        }
    }

    private void addActionPerformed() {
        severityModel.insertRow(0, new Object[]{"", Color.BLACK});
        severityNameTable.changeSelection(0, 0,
                false, false);
        List<Severity> serverities = ts.getAllSeverities();
        Severity severity = new Severity("", Color.BLACK);
        serverities.add(severity);
        ts.addSeverities(serverities);
    }

    private void applySeverityActionPerformed() {
        List<Object[]> list = new ArrayList<Object[]>();

        for (int i = 0; i < severityModel.getRowCount(); i++) {
            list.add(new Object[]{(String) severityModel.getValueAt(i, 0), (Color) severityModel.getValueAt(i, 1)});
        }

        ts.applySeverities(list);
        loadSeverity();
        removeLanguageTabs();
        loadLanguageTabs();
    }

    private void cancelActionPerformed() {
        dispose();
    }

    private void jButton1ActionPerformed() {
    }

    private void loadSeverity() {
        clearModel(severityModel);
        List<Severity> severities = ts.getAllSeverities();
        for (Severity severity : severities) {
            severityModel.addRow(new Object[]{severity.toString(),
                        severity.getColor()});
            severityModel.setRowColour(severityModel.getRowCount() - 1, severity.getColor());
        }

    }

    private void clearModel(ColorTableModel model) {
        while (0 < model.getRowCount()) {
            model.removeRow(0);
        }
    }

    private void loadLanguageTabs() {
        for (String language : ts.getAvailableLanguages()) {
            LanguageSeverityConfiguration lcp = new LanguageSeverityConfiguration(
                    language, ts.getViolationTypes(language), ts.getRuletypes(language), ts.getAllSeverities(), ts);
            jTabbedPane1.addTab(language, lcp);
        }
        if (ts.getAvailableLanguages().length == 0) {
            jTabbedPane1.addTab(ResourceBundles.getValue("No programming language availible"), new JPanel());
        }
    }

    private void removeLanguageTabs() {
        while (jTabbedPane1.getTabCount() > 1) {
            jTabbedPane1.remove(1);
        }
    }
}
