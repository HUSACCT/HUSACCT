package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
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
	
	private final String language;
	private final HashMap<String, List<RuleType>> ruletypes;
	private final Map<String, List<ViolationType>> violationTypes;
	private final TaskServiceImpl taskServiceImpl;
	
	private List<Severity> severityNames;

	private ActiveViolationPanel activeViolationtype;
	private RuleTypeSeverityPanel ruletypeSeverity;
	private ViolationTypeSeverityPanel violationtypeSeverity;
	private JTabbedPane tabbedPane;

	public LanguageSeverityConfiguration(String language, Map<String, List<ViolationType>> violationTypes,
			HashMap<String, List<RuleType>> ruletypes, TaskServiceImpl ts, List<Severity> severities) {
		
		this.severityNames = severities;
		this.language = language;
		this.ruletypes = ruletypes;
		this.taskServiceImpl = ts;
		this.violationTypes = violationTypes;

		initComponents();
	}

	private void initComponents() {		
		tabbedPane = new JTabbedPane();
		
		ruletypeSeverity = new RuleTypeSeverityPanel(taskServiceImpl, this, ruletypes, language);
		violationtypeSeverity = new ViolationTypeSeverityPanel(taskServiceImpl, this, violationTypes, language);
		activeViolationtype = new ActiveViolationPanel(taskServiceImpl, ruletypes, language);
		
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
		severityNames = severities;
	}
	
	public List<Severity> getSeverityNames(){
		return severityNames;
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
