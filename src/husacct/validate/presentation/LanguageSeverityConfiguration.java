package husacct.validate.presentation;

import husacct.validate.abstraction.language.ValidateTranslator;
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
	private static final long serialVersionUID = 4125846168658642242L;
	
	private List<Severity> severityNames;
	
	private final String language;
	private final HashMap<String, List<RuleType>> ruletypes;
	private final Map<String, List<ViolationType>> violationTypes;
	private final TaskServiceImpl taskServiceImpl;

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
		
		tabbedPane.addTab(ValidateTranslator.getValue("SetRuletypeSeverity"), ruletypeSeverity);
		tabbedPane.addTab(ValidateTranslator.getValue("SetViolationSeverity"), violationtypeSeverity);
		tabbedPane.addTab(ValidateTranslator.getValue("SetViolationtypeActivePerRuletype"), activeViolationtype);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).
				addComponent(tabbedPane));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).
				addGroup(GroupLayout.Alignment.TRAILING, layout.
				createSequentialGroup().addContainerGap().addComponent(
				tabbedPane)));
	}
	
	public void setSeverityNames(List<Severity> severities){
		severityNames = severities;
	}
	
	public List<Severity> getSeverityNames(){
		return severityNames;
	}
	
	public void setText(){
		tabbedPane.setTitleAt(0, ValidateTranslator.getValue("SetRuletypeSeverity"));
		tabbedPane.setTitleAt(1, ValidateTranslator.getValue("SetViolationSeverity"));
		tabbedPane.setTitleAt(2, ValidateTranslator.getValue("SetViolationtypeActivePerRuletype"));
		
		ruletypeSeverity.setText();
		violationtypeSeverity.setText();
		activeViolationtype.setText();
	}
}
