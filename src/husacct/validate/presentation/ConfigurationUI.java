package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.Severity;
import husacct.validate.presentation.languageSeverityConfiguration.ConfigurationRuleTypeDTO;
import husacct.validate.presentation.languageSeverityConfiguration.ConfigurationViolationTypeDTO;
import husacct.validate.presentation.languageSeverityConfiguration.LanguageSeverityConfigurationPanel;
import husacct.validate.presentation.tableModels.ColorTableModel;
import husacct.validate.task.TaskServiceImpl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

public final class ConfigurationUI extends HelpableJInternalFrame implements Observer {

	private static final long serialVersionUID = 7721461596323704063L;
	private static Logger logger = Logger.getLogger(ConfigurationUI.class);
	private TaskServiceImpl taskServiceImpl;
	private ColorTableModel severityModel;
	private List<Severity> severities;
	private JButton cancel, applySeverity, restore;
	private JTabbedPane tabPanel;
	private JPanel severityNamePanel;
	private JScrollPane severityNameScrollPane;
	private JTable severityNameTable;
	private List<LanguageSeverityConfigurationPanel> tabs;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public ConfigurationUI(TaskServiceImpl ts) {
		tabs = new ArrayList<LanguageSeverityConfigurationPanel>();

		taskServiceImpl = ts;
		taskServiceImpl.subscribe(this);

		initComponents();
		loadAfterChange();
	}

	private void initComponents() {

		tabPanel = new JTabbedPane();
		severityNamePanel = new JPanel();
		severityNameScrollPane = new JScrollPane();
		severityNameTable = new JTable();
		severityNameTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

		tabPanel.addTab(localeService.getTranslatedString("SeverityConfiguration"), severityNamePanel);

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

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelActionPerformed();
			}
		});

		createSeverityPanelLayout();
		createRootLayout();
	}

	private void createSeverityPanelLayout() {
		GroupLayout severityNamePanelLayout = new GroupLayout(severityNamePanel);

		GroupLayout.ParallelGroup horizontalButtonGroup = severityNamePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false);
		horizontalButtonGroup.addComponent(restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonGroup.addComponent(applySeverity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

		GroupLayout.ParallelGroup severityNameGroup = severityNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
		severityNameGroup.addComponent(severityNameScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

		GroupLayout.SequentialGroup horizontalPaneGroup = severityNamePanelLayout.createSequentialGroup();
		horizontalPaneGroup.addGroup(severityNameGroup);
		horizontalPaneGroup.addContainerGap();
		horizontalPaneGroup.addGroup(horizontalButtonGroup);

		severityNamePanelLayout.setHorizontalGroup(horizontalPaneGroup);

		GroupLayout.SequentialGroup verticalButtonGroup = severityNamePanelLayout.createSequentialGroup();
		verticalButtonGroup.addContainerGap();
		verticalButtonGroup.addComponent(restore);
		verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalButtonGroup.addComponent(applySeverity);
		verticalButtonGroup.addContainerGap();

		GroupLayout.ParallelGroup verticalPaneGroup = severityNamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
		verticalPaneGroup.addComponent(severityNameScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		verticalPaneGroup.addGroup(verticalButtonGroup);

		severityNamePanelLayout.setVerticalGroup(verticalPaneGroup);
		severityNamePanel.setLayout(severityNamePanelLayout);
	}

	private void createRootLayout() {
		GroupLayout baseLayout = new GroupLayout(getRootPane());

		GroupLayout.ParallelGroup horizontalGroup = baseLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
		horizontalGroup.addComponent(tabPanel);
		horizontalGroup.addComponent(cancel);

		baseLayout.setHorizontalGroup(horizontalGroup);

		GroupLayout.SequentialGroup verticalGroup = baseLayout.createSequentialGroup();
		verticalGroup.addComponent(tabPanel);
		verticalGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		verticalGroup.addComponent(cancel);
		verticalGroup.addGap(16, 16, 16);

		baseLayout.setVerticalGroup(verticalGroup);

		getRootPane().setLayout(baseLayout);
	}

	@Override
	public void update(Observable o, Object arg) {
		loadAfterChange();
	}

	public void loadAfterChange() {
		setText();
		loadModels();
		loadSeverity();
		setLanguageTabsLanguage();
	}

	private void applySeverityActionPerformed() {
		for (int i = 0; i < severityModel.getRowCount(); i++) {
			try {
				Severity severity = severities.get(i);
				severity.setColor((Color) severityModel.getValueAt(i, 1));
				severities.set(i, severity);
			} catch (IndexOutOfBoundsException e) {
				severities.add(new Severity((String) severityModel.getValueAt(i, 0), (Color) severityModel.getValueAt(i, 1)));
			}
		}

		taskServiceImpl.addSeverities(severities);
		loadSeverity();
		reloadTableModels();
		clearSelections();
	}

	private void reloadTableModels() {
		for (LanguageSeverityConfigurationPanel s : tabs) {
			s.reloadTableModel();
		}
	}

	private void clearSelections() {
		for (LanguageSeverityConfigurationPanel s : tabs) {
			s.clearSelection();
			s.selectFirstCategory();
		}
	}

	private void cancelActionPerformed() {
		dispose();
	}

	private void restore() {
		taskServiceImpl.restoreSeveritiesToDefault();
		loadSeverity();
	}

	public void setText() {
		setTitle(localeService.getTranslatedString("ValidateConfigurationTitle"));
		applySeverity.setText(localeService.getTranslatedString("Apply"));
		restore.setText(localeService.getTranslatedString("RestoreToDefault"));
		cancel.setText(localeService.getTranslatedString("Cancel"));
	}

	private void loadModels() {
		severityModel = new ColorTableModel();
		severityNameTable.setModel(severityModel);
		severityModel.setColorEditor(severityNameTable, 1);
	}

	private void loadSeverity() {
		clearModel(severityModel);
		severities = taskServiceImpl.getAllSeverities();
		for (Severity severity : severities) {
			severityModel.addRow(new Object[] {severity.getSeverityKeyTranslated(), severity.getColor()});
		}

	}

	private void clearModel(ColorTableModel model) {
		while (0 < model.getRowCount()) {
			model.removeRow(0);
		}
	}

	private void setLanguageTabsLanguage() {
		if (tabPanel.getTabCount() == 1) {
			loadLanguageTabs();
			return;
		}
		for (LanguageSeverityConfigurationPanel panel : tabs) {
			panel.loadAfterChange();
		}
	}

	private void loadLanguageTabs() {
		for (String language : taskServiceImpl.getAvailableLanguages()) {
			ConfigurationRuleTypeDTO configurationRuleTypeDTO = new ConfigurationRuleTypeDTO(language, severities, taskServiceImpl.getRuletypes(language));
			ConfigurationViolationTypeDTO configurationViolationTypeDTO = new ConfigurationViolationTypeDTO(language, severities, taskServiceImpl.getViolationTypes(language));

			LanguageSeverityConfigurationPanel lcp = new LanguageSeverityConfigurationPanel(configurationRuleTypeDTO, configurationViolationTypeDTO, taskServiceImpl);
			tabPanel.addTab(language, lcp);
			tabs.add(lcp);
		}
		if (tabPanel.getTabCount() == 1) {
			logger.error("No programming language set");
			tabPanel.addTab(localeService.getTranslatedString("NoProgrammingLanguageAvailable"), new JPanel());
		}
	}
}