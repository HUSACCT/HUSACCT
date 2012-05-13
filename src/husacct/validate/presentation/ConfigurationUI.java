package husacct.validate.presentation;

import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.validation.Severity;
import husacct.validate.presentation.tableModels.ColorTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;

public final class ConfigurationUI extends javax.swing.JInternalFrame {

	private static final long serialVersionUID = 3568220674416621458L;
	private TaskServiceImpl taskServiceImpl;
	private ColorTableModel severityModel;
	private List<Severity> severities;

	private JButton add, remove, down, up, cancel, applySeverity, restore;
	private JTabbedPane jTabbedPane1;
	private JPanel severityNamePanel;
	private JScrollPane severityNameScrollPane;
	private JTable severityNameTable;
	
	private static Logger logger = Logger.getLogger(ConfigurationUI.class);

	public ConfigurationUI(TaskServiceImpl ts) {
		this.taskServiceImpl = ts;
		severities = ts.getAllSeverities();
		initComponents();
		loadGUIText();
	}

	private void initComponents() {

		jTabbedPane1 = new JTabbedPane();
		severityNamePanel = new JPanel();
		severityNameScrollPane = new JScrollPane();
		severityNameTable = new JTable();
		severityNameTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

		severityNameTable.setFillsViewportHeight(true);
		severityNameTable.getTableHeader().setReorderingAllowed(false);
		severityNameScrollPane.setViewportView(severityNameTable);

		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				addActionPerformed();
			}
		});

		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				removeActionPerformed();
			}
		});

		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				upActionPerformed();
			}
		});

		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				downActionPerformed();
			}
		});

		applySeverity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				applySeverityActionPerformed();
			}
		});

		restore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				restore();
			}
		});

		javax.swing.GroupLayout severityNamePanelLayout = new javax.swing.GroupLayout(severityNamePanel);
		severityNamePanel.setLayout(severityNamePanelLayout);
		severityNamePanelLayout.setHorizontalGroup(
				severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(severityNamePanelLayout.createSequentialGroup().addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(remove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(up, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(restore).addComponent(applySeverity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(down, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
		severityNamePanelLayout.setVerticalGroup(
				severityNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(severityNameScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE).addGroup(severityNamePanelLayout.createSequentialGroup().addContainerGap().addComponent(add).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(remove).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(up).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(down).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(restore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(applySeverity).addContainerGap()));

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

	//Button Action
	private void downActionPerformed() {
		if (severityNameTable.getSelectedRow() < severityNameTable.getRowCount() - 1) {
			severityModel.moveRow(severityNameTable.getSelectedRow(), severityNameTable.getSelectedRow(), severityNameTable.getSelectedRow() + 1);
			severityNameTable.changeSelection(severityNameTable.getSelectedRow() + 1, 0, false, false);
			Severity severity = severities.get(severityNameTable.getSelectedRow());
			severities.remove(severityNameTable.getSelectedRow());
			severities.add(severityNameTable.getSelectedRow() + 1, severity);
		}
	}

	private void upActionPerformed() {
		if (severityNameTable.getSelectedRow() > 0) {
			severityModel.moveRow(severityNameTable.getSelectedRow(), severityNameTable.getSelectedRow(), severityNameTable.getSelectedRow() - 1);
			severityNameTable.changeSelection(severityNameTable.getSelectedRow() - 1, 0, false, false);
			Severity severity = severities.get(severityNameTable.getSelectedRow());
			severities.remove(severityNameTable.getSelectedRow());
			severities.add(severityNameTable.getSelectedRow() - 1, severity);
		}
	}

	private void removeActionPerformed() {
		if (severityNameTable.getRowCount() > 1 && severityNameTable.getSelectedRow() > -1) {
			severityModel.removeRow(severityNameTable.getSelectedRow());
			severities.remove(severityNameTable.getSelectedRow());
		}
	}

	private void addActionPerformed() {
		severityModel.insertRow(0, new Object[]{"", Color.BLACK});
		severityNameTable.changeSelection(0, 0, false, false);
		Severity severity = new Severity("", Color.black);
		severities.add(0, severity);
	}

	private void applySeverityActionPerformed() {
		
		for (int i = 0; i < severityModel.getRowCount(); i++) {
			try{
				Severity severity = severities.get(i);
				severity.setUserName((String) severityModel.getValueAt(i, 0));
				severity.setColor((Color) severityModel.getValueAt(i, 1));
				severities.set(i, severity);
			} catch (IndexOutOfBoundsException e){
				severities.add(new Severity((String) severityModel.getValueAt(i, 0), (Color) severityModel.getValueAt(i, 1)));
			}
		}
		
//		taskServiceImpl.applySeverities(list);
		taskServiceImpl.addSeverities(severities);
		loadSeverity();
		removeLanguageTabs();
		loadLanguageTabs();
	}

	private void cancelActionPerformed() {
		dispose();
	}

	private void restore() {
		taskServiceImpl.restoreSeveritiesToDefault();
		loadSeverity();
	}
	
	//User Functions
	public void loadGUIText(){
		setTitle(ValidateTranslator.getValue("Configuration"));
		add.setText(ValidateTranslator.getValue("Add"));
		remove.setText(ValidateTranslator.getValue("Remove"));
		up.setText(ValidateTranslator.getValue("Up"));
		down.setText(ValidateTranslator.getValue("Down"));
		applySeverity.setText(ValidateTranslator.getValue("Apply"));
		restore.setText(ValidateTranslator.getValue("RestoreToDefault"));
		jTabbedPane1.addTab(ValidateTranslator.getValue("SeverityConfiguration"), severityNamePanel);
		cancel.setText(ValidateTranslator.getValue("Cancel"));

		loadModels();
		removeLanguageTabs();
		loadLanguageTabs();
	}
	
	private void loadModels(){
		severityModel = new ColorTableModel();
		severityNameTable.setModel(severityModel);
		severityModel.setColorEditor(severityNameTable, 1);
		loadSeverity();
	}

	private void loadSeverity() {
		clearModel(severityModel);
		severities = taskServiceImpl.getAllSeverities();
		for (Severity severity : severities) {
			severityModel.addRow(new Object[]{severity.toString(), severity.getColor()});
		}

	}

	private void clearModel(ColorTableModel model) {
		while (0 < model.getRowCount()) {
			model.removeRow(0);
		}
	}

	private void loadLanguageTabs() {
		for (String language : taskServiceImpl.getAvailableLanguages()) {
			LanguageSeverityConfiguration lcp = new LanguageSeverityConfiguration(language, taskServiceImpl.getViolationTypes(language), taskServiceImpl.getRuletypes(language), taskServiceImpl.getAllSeverities(), taskServiceImpl);
			jTabbedPane1.addTab(language, lcp);
		}
		if (taskServiceImpl.getAvailableLanguages().length == 0) {
			logger.error("No programming language set");
			jTabbedPane1.addTab(ValidateTranslator.getValue("NoProgrammingLanguageAvailible"), new JPanel());
		}
	}

	private void removeLanguageTabs() {
		for(int i = 0; i < jTabbedPane1.getTabCount(); i++){
			final String tabTitle = jTabbedPane1.getTitleAt(i);
			for(String language : taskServiceImpl.getAvailableLanguages()){
				if(tabTitle.equals(language)){
					jTabbedPane1.remove(i);
					i--;
				}
			}
		}
	}
}