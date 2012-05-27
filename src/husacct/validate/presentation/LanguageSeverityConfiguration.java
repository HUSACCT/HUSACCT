package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.ConfigurationRuleTypeDTO;
import husacct.validate.domain.validation.iternal_tranfer_objects.ConfigurationViolationTypeDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.languageSeverityConfiguration.ActiveViolationPanel;
import husacct.validate.presentation.languageSeverityConfiguration.RuleTypeSeverityPanel;
import husacct.validate.presentation.languageSeverityConfiguration.ViolationTypeSeverityPanel;
import husacct.validate.task.TaskServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class LanguageSeverityConfiguration extends JPanel {

	private static final long serialVersionUID = 6607502138038915874L;
	
	private final TaskServiceImpl taskServiceImpl;
	private final ConfigurationRuleTypeDTO configurationSubPanelDTO;
	private final ConfigurationViolationTypeDTO configurationViolationTypeDTO;

	private ActiveViolationPanel activeViolationtype;
	private RuleTypeSeverityPanel ruletypeSeverity;
	private ViolationTypeSeverityPanel violationtypeSeverity;
	private JTabbedPane tabbedPane;

	public LanguageSeverityConfiguration(ConfigurationRuleTypeDTO configurationRuleTypeDTO, ConfigurationViolationTypeDTO configurationViolationTypeDTO, TaskServiceImpl ts) {
		
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
		
		tabbedPane.addTab(ServiceProvider.getInstance().getControlService().getTranslatedString("SetRuletypeSeverity"), ruletypeSeverity);
		tabbedPane.addTab(ServiceProvider.getInstance().getControlService().getTranslatedString("SetViolationSeverity"), violationtypeSeverity);
		tabbedPane.addTab(ServiceProvider.getInstance().getControlService().getTranslatedString("SetViolationtypeActivePerRuletype"), activeViolationtype);
		
		createLayout();
	}
	
	private void createLayout(){
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
	
	public void setSeverityNames(List<Severity> severities){
		violationtypeSeverity.setSeverities(severities);
	}
	
	public void loadAfterChange(){
		setText();
		
		ruletypeSeverity.loadAfterChange();
		violationtypeSeverity.loadAfterChange();
		activeViolationtype.loadAfterChange();
	}
	
	private void setText(){
		tabbedPane.setTitleAt(0, ServiceProvider.getInstance().getControlService().getTranslatedString("SetRuletypeSeverity"));
		tabbedPane.setTitleAt(1, ServiceProvider.getInstance().getControlService().getTranslatedString("SetViolationSeverity"));
		tabbedPane.setTitleAt(2, ServiceProvider.getInstance().getControlService().getTranslatedString("SetViolationtypeActivePerRuletype"));
	}
}
