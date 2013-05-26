package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.task.TaskServiceImpl;

import java.awt.Dimension;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class LanguageSeverityConfigurationPanel extends JPanel {

	private static final long serialVersionUID = 6607502138038915874L;
	private final TaskServiceImpl taskServiceImpl;
	private final ConfigurationRuleTypeDTO configurationSubPanelDTO;
	private final ConfigurationViolationTypeDTO configurationViolationTypeDTO;
	private ActiveViolationPanel activeViolationtype;
	private RuleTypeSeverityPanel ruletypeSeverity;
	private ViolationTypeSeverityPanel violationtypeSeverity;
	private JTabbedPane tabbedPane;

	public LanguageSeverityConfigurationPanel(ConfigurationRuleTypeDTO configurationRuleTypeDTO, ConfigurationViolationTypeDTO configurationViolationTypeDTO, TaskServiceImpl ts) {
		this.configurationSubPanelDTO = configurationRuleTypeDTO;
		this.configurationViolationTypeDTO = configurationViolationTypeDTO;
		this.taskServiceImpl = ts;

		initComponents();
	}

	private void initComponents() {
		tabbedPane = new JTabbedPane();

		ruletypeSeverity = new RuleTypeSeverityPanel(taskServiceImpl, configurationSubPanelDTO);
		violationtypeSeverity = new ViolationTypeSeverityPanel(taskServiceImpl, configurationViolationTypeDTO);
		activeViolationtype = new ActiveViolationPanel(taskServiceImpl, configurationSubPanelDTO.getRuletypes(), configurationSubPanelDTO.getLanguage());

		tabbedPane.addTab(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SetRuletypeSeverity"), ruletypeSeverity);
		tabbedPane.addTab(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SetViolationSeverity"), violationtypeSeverity);
		tabbedPane.addTab(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SetViolationtypeActivePerRuletype"), activeViolationtype);

		createLayout();
	}

	private void createLayout() {
		GroupLayout layout = new GroupLayout(this);

		GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
		horizontalGroup.addComponent(tabbedPane);
		layout.setHorizontalGroup(horizontalGroup);

		GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
		verticalGroup.addContainerGap();
		verticalGroup.addComponent(tabbedPane);
		layout.setVerticalGroup(verticalGroup);

		this.setLayout(layout);
	}

	void setSeverityNames(List<Severity> severities) {
		ruletypeSeverity.setSeverities(severities);
		violationtypeSeverity.setSeverities(severities);
	}

	public void loadAfterChange() {
		setText();

		ruletypeSeverity.loadAfterChange();
		violationtypeSeverity.loadAfterChange();
		activeViolationtype.loadAfterChange();
	}

	private void setText() {
		tabbedPane.setTitleAt(0, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SetRuletypeSeverity"));
		tabbedPane.setTitleAt(1, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SetViolationSeverity"));
		tabbedPane.setTitleAt(2, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SetViolationtypeActivePerRuletype"));
	}

	public void clearSelection() {
		activeViolationtype.clearSelection();
		ruletypeSeverity.clearSelection();
		violationtypeSeverity.clearSelection();
	}

	public void selectFirstCategory() {
		activeViolationtype.selectFirstIndexOfCategory();
		ruletypeSeverity.selectFirstIndexOfCategory();
		violationtypeSeverity.selectFirstIndexOfCategory();
	}

	public void reloadTableModel() {
		ruletypeSeverity.loadModel();
		violationtypeSeverity.loadModel();
	}
}