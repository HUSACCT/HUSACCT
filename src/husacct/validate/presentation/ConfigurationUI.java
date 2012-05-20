package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.control.IControlService;
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
	private static Logger logger = Logger.getLogger(ConfigurationUI.class);
	
	private TaskServiceImpl taskServiceImpl;
	private ColorTableModel severityModel;
	private List<Severity> severities;

	private JButton add, remove, down, up, cancel, applySeverity, restore;
	private JTabbedPane tabPanel;
	private JPanel severityNamePanel;
	private JScrollPane severityNameScrollPane;
	private JTable severityNameTable;
	private List<LanguageSeverityConfiguration> tabs;

	public ConfigurationUI(TaskServiceImpl ts) {
		tabs = new ArrayList<LanguageSeverityConfiguration>();
		
		this.taskServiceImpl = ts;
		severities = ts.getAllSeverities();
		
		initComponents();
		loadGUIText();
	}

	private void initComponents() {

		tabPanel = new JTabbedPane();
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
				if(severityNameTable.getSelectedRow() > -1){
					removeActionPerformed();
				} else {
					ServiceProvider.getInstance().getControlService().showErrorMessage("SelectRowFirst");
				}
			}
		});

		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if(severityNameTable.getSelectedRow() > -1){
					upActionPerformed();
				} else {
					ServiceProvider.getInstance().getControlService().showErrorMessage("SelectRowFirst");
				}
			}
		});

		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if(severityNameTable.getSelectedRow() > -1){
					downActionPerformed();
				} else {
					ServiceProvider.getInstance().getControlService().showErrorMessage("SelectRowFirst");
				}
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

		GroupLayout severityNamePanelLayout = new GroupLayout(severityNamePanel);
		severityNamePanel.setLayout(severityNamePanelLayout);
		
		severityNamePanelLayout.setHorizontalGroup(severityNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(severityNamePanelLayout.createSequentialGroup()
				.addComponent(severityNameScrollPane, GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(severityNamePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
					.addComponent(remove, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(add, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(up, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(applySeverity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(down, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				)
				.addContainerGap()
			)
		);
		
		severityNamePanelLayout.setVerticalGroup(severityNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(severityNameScrollPane, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
			.addGroup(severityNamePanelLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(add)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(remove)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(up)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(down)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(restore)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(applySeverity)
				.addContainerGap()
			)
		);

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelActionPerformed();
			}
		});

		GroupLayout baseLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(baseLayout);
		
		baseLayout.setHorizontalGroup(
			baseLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addComponent(tabPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
			.addGroup(baseLayout.createSequentialGroup().addContainerGap().addComponent(cancel).addContainerGap())
		);
		baseLayout.setVerticalGroup(
			baseLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
			.addGroup(baseLayout.createSequentialGroup()
				.addComponent(tabPanel)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(cancel)
				.addGap(16, 16, 16)
			)
		);
	}

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
			severities.remove(severityNameTable.getSelectedRow());
			severityModel.removeRow(severityNameTable.getSelectedRow());
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
			if(severityModel.getValueAt(i, 0).toString().isEmpty()){
				IControlService controlServiceImpl = ServiceProvider.getInstance().getControlService();
				controlServiceImpl.showErrorMessage("SeverityNameNotSet, Change The name to save the severities");
				return;
			}
			try{
				Severity severity = severities.get(i);
				if(!severity.getDefaultName().equals((String) severityModel.getValueAt(i, 0))){
					severity.setName((String) severityModel.getValueAt(i, 0));
				}
				severity.setColor((Color) severityModel.getValueAt(i, 1));
				severities.set(i, severity);
			} catch (IndexOutOfBoundsException e){
				severities.add(new Severity((String) severityModel.getValueAt(i, 0), (Color) severityModel.getValueAt(i, 1)));
			}
		}
		
		taskServiceImpl.addSeverities(severities);
		loadSeverity();
	}

	private void cancelActionPerformed() {
		dispose();
	}

	private void restore() {
		taskServiceImpl.restoreSeveritiesToDefault();
		loadSeverity();
	}
	
	public void loadGUIText(){
		setTitle(ValidateTranslator.getValue("Configuration"));
		add.setText(ValidateTranslator.getValue("Add"));
		remove.setText(ValidateTranslator.getValue("Remove"));
		up.setText(ValidateTranslator.getValue("Up"));
		down.setText(ValidateTranslator.getValue("Down"));
		applySeverity.setText(ValidateTranslator.getValue("Apply"));
		restore.setText(ValidateTranslator.getValue("RestoreToDefault"));
		tabPanel.addTab(ValidateTranslator.getValue("SeverityConfiguration"), severityNamePanel);
		cancel.setText(ValidateTranslator.getValue("Cancel"));

		loadModels();
		setLanguageTabsLanguage();
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
	
	private void setLanguageTabsLanguage(){
		if(tabPanel.getTabCount() == 1){
			loadLanguageTabs();
			return;
		}
		for (LanguageSeverityConfiguration panel : tabs){
			panel.loadAfterChange();
		}
	}
	
	private void loadLanguageTabs() {
		for (String language : taskServiceImpl.getAvailableLanguages()) {
			LanguageSeverityConfiguration lcp = new LanguageSeverityConfiguration(language, taskServiceImpl.getViolationTypes(language), taskServiceImpl.getRuletypes(language), taskServiceImpl, severities);
			tabPanel.addTab(language, lcp);
			tabs.add(lcp);
		}
		if (tabPanel.getTabCount() == 1) {
			logger.error("No programming language set");
			tabPanel.addTab(ValidateTranslator.getValue("NoProgrammingLanguageAvailible"), new JPanel());
		}
	}
}