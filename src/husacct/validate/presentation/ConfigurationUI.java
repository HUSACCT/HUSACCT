package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.presentation.languageSeverityConfiguration.DTO.ConfigurationRuleTypeDTO;
import husacct.validate.presentation.languageSeverityConfiguration.DTO.ConfigurationViolationTypeDTO;
import husacct.validate.presentation.languageSeverityConfiguration.LanguageSeverityConfigurationPanel;
import husacct.validate.task.TaskServiceImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;

import org.apache.log4j.Logger;

public final class ConfigurationUI extends JInternalFrame implements Observer {

	private static final long serialVersionUID = 7721461596323704063L;
	private static Logger logger = Logger.getLogger(ConfigurationUI.class);
	private TaskServiceImpl taskServiceImpl;
	private List<Severity> severities;
	private JButton cancel;
	private JTabbedPane tabPanel;
	private List<LanguageSeverityConfigurationPanel> tabs;

	public ConfigurationUI(TaskServiceImpl ts) {
		tabs = new ArrayList<LanguageSeverityConfigurationPanel>();

		taskServiceImpl = ts;
		taskServiceImpl.subscribe(this);
		severities = taskServiceImpl.getAllSeverities();

		initComponents();
		loadAfterChange();
	}

	private void initComponents() {

		tabPanel = new JTabbedPane();
		cancel = new JButton();

		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelActionPerformed();
			}
		});

		createRootLayout();
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
		setLanguageTabsLanguage();
	}

	private void cancelActionPerformed() {
		dispose();
	}

	public void setText() {
		setTitle(ServiceProvider.getInstance().getControlService().getTranslatedString("ValidateConfigurationTitle"));
		cancel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Cancel"));
	}
	
	private void setLanguageTabsLanguage() {
		if (tabPanel.getTabCount() == 0) {
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
		if (tabPanel.getTabCount() == 0) {
			logger.error("No programming language set");
			tabPanel.addTab(ServiceProvider.getInstance().getControlService().getTranslatedString("NoProgrammingLanguageAvailible"), new JPanel());
		}
	}
}