package husacct.validate.domain.validation.ruletype;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.check.util.CheckConformanceUtilSeverity;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.internaltransferobjects.Mappings;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.EnumSet;
import java.util.List;

public abstract class RuleType {
	protected final String key;
	protected final String descriptionKey;
	protected final String categoryKey;
	protected final EnumSet<RuleTypes> exceptionRuleKeys;
	protected final List<ViolationType> violationTypes;
	protected List<RuleType> exceptionRules;
	protected final Severity severity;
	protected List<Violation> violations;
	protected Mappings mappings;
	protected List<Mapping> physicalClasspathsFrom;
	protected final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
	protected final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	private AbstractViolationType violationtypefactory;

	public RuleType(String key, String categoryKey, List<ViolationType> violationtypes, EnumSet<RuleTypes> exceptionRuletypes, Severity severity) {
		this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
		this.violationTypes = violationtypes;
		this.exceptionRuleKeys = exceptionRuletypes;
		this.severity = severity;
	}

	public String getKey() {
		return key;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public EnumSet<RuleTypes> getExceptionRuleKeys() {
		return exceptionRuleKeys;
	}

	public List<ViolationType> getViolationTypes() {
		return violationTypes;
	}

	public void setExceptionrules(List<RuleType> ruletypes) {
		this.exceptionRules = ruletypes;
	}

	public List<RuleType> getExceptionrules() {
		return exceptionRules;
	}

	public Severity getSeverity() {
		return severity;
	}

	public abstract List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule);

	protected Violation createViolation(RuleDTO rootRule, Mapping classPathFrom, Mapping classPathTo, DependencyDTO dependency, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Message message = new Message(rootRule);

		LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
		LogicalModule logicalModuleTo = new LogicalModule(classPathTo);
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);

		final Severity violationTypeSeverity = getViolationTypeSeverity(dependency.type);
		Severity severity = CheckConformanceUtilSeverity.getSeverity(configuration, this.severity, violationTypeSeverity);

		Violation newViolation = new Violation()
				.setLineNumber(dependency.lineNumber)
				.setSeverity(severity.clone())
				.setRuletypeKey(this.key)
				.setViolationtypeKey(dependency.type)
				.setClassPathFrom(dependency.from)
				.setClassPathTo(dependency.to)
				.setInDirect(dependency.isIndirect)
				.setMessage(message)
				.setLogicalModules(logicalModules);
		return newViolation;
	}

	protected Violation createViolation(RuleDTO rootRule, LogicalModules logicalModules, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Message message = new Message(rootRule);
		Severity severity = CheckConformanceUtilSeverity.getSeverity(configuration, this.severity, null);

		Violation newViolation = new Violation();
		newViolation = newViolation
				.setSeverity(severity.clone())
				.setRuletypeKey(this.key)
				.setInDirect(false)
				.setMessage(message)
				.setLogicalModules(logicalModules);
		return newViolation;
	}

	protected Violation createViolation(RuleDTO rootRule, Mapping classPathFrom, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Message message = new Message(rootRule);

		LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom);

		Severity severity = CheckConformanceUtilSeverity.getSeverity(configuration, this.severity, null);

		Violation newViolation = new Violation();
		newViolation = newViolation
				.setSeverity(severity.clone())
				.setRuletypeKey(this.key)
				.setClassPathFrom(classPathFrom.getPhysicalPath())
				.setInDirect(false)
				.setMessage(message)
				.setLogicalModules(logicalModules);
		return newViolation;
	}

	protected Violation createViolation(RuleDTO rootRule, Mapping classPathFrom, String violationTypeKey, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Message message = new Message(rootRule);
		LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom);

		final Severity violationTypeSeverity = getViolationTypeSeverity(violationTypeKey);

		Severity severity = CheckConformanceUtilSeverity.getSeverity(configuration, this.severity, violationTypeSeverity);

		Violation newViolation = new Violation();
		newViolation = newViolation
				.setSeverity(severity.clone())
				.setRuletypeKey(this.key)
				.setViolationtypeKey(violationTypeKey)
				.setClassPathFrom(classPathFrom.getPhysicalPath())
				.setInDirect(false)
				.setMessage(message)
				.setLogicalModules(logicalModules);
		return newViolation;
	}

	private void initializeViolationTypeFactory(ConfigurationServiceImpl configuration) {
		if (violationtypefactory == null) {
			this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
		}
	}

	private Severity getViolationTypeSeverity(String violationTypeKey) {
		try {
			return violationtypefactory.createViolationType(this.key, violationTypeKey).getSeverity();
		} catch (ViolationTypeNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean equals(RuleTypes desiredRuleType) {
		return this.getKey().equals(desiredRuleType.toString());
	}
}