package husacct.validate.domain.validation.ruletype;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.internaltransferobjects.Mapping;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public abstract class RuleType {
	protected final String key;
	protected final String descriptionKey;
	protected final String categoryKey;
	protected final EnumSet<RuleTypes> exceptionRuleTypeKeys;
	protected final List<ViolationType> violationTypes;
	protected List<RuleType> exceptionRules;
	protected final Severity severity;
	protected List<Violation> violations;
	protected List<Mapping> fromMappings;
	protected List<Mapping> toMappings;
	protected final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
	protected final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	private AbstractViolationType violationTypeFactory;

	public RuleType(String key, String categoryKey, List<ViolationType> violationTypes, EnumSet<RuleTypes> exceptionRuleTypeKeys, Severity severity) {
		this.violations = new ArrayList<>();
        this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
		this.violationTypes = violationTypes;
		this.exceptionRuleTypeKeys = exceptionRuleTypeKeys;
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

	public EnumSet<RuleTypes> getExceptionRuleTypeKeys() {
		return exceptionRuleTypeKeys;
	}

	public List<ViolationType> getViolationTypes() {
		return violationTypes;
	}

	public void setExceptionRules(List<RuleType> ruleTypes) {
		this.exceptionRules = ruleTypes;
	}

	public List<RuleType> getExceptionRules() {
		return exceptionRules;
	}

	public Severity getSeverity() {
		return severity;
	}

	public abstract List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO currentRule);

	protected ArrayList<Mapping> getAllClasspathsOfModule(ModuleDTO module, String[] violationTypeKeys) {
		HashSet<Mapping> classpathsFrom = new HashSet<>();
		List<String> physicalClassPaths = new ArrayList<>();
		physicalClassPaths.addAll(defineService.getModule_AllPhysicalClassPathsOfModule(module.logicalPath));
		for (String classpath : physicalClassPaths) {
			Mapping mapping = new Mapping(module.logicalPath, module.type, classpath, violationTypeKeys);
			classpathsFrom.add(mapping);
		}
		return new ArrayList<>(classpathsFrom);
	}

	protected ArrayList<Mapping> getAllPhysicalPackagePathsOfModule(ModuleDTO module, String[] violationTypeKeys) {
		HashSet<Mapping> packagePathsFrom = new HashSet<>();
		List<String> physicalPackagePaths = new ArrayList<>();
		physicalPackagePaths.addAll(defineService.getModule_AllPhysicalPackagePathsOfModule(module.logicalPath));
		for (String classpath : physicalPackagePaths) {
			Mapping mapping = new Mapping(module.logicalPath, module.type, classpath, violationTypeKeys);
			packagePathsFrom.add(mapping);
		}
		return new ArrayList<>(packagePathsFrom);
	}

	protected HashSet<String> getAllExceptionFromTos(RuleDTO rule){
		HashSet<String> exceptionClassPathFromTos = new HashSet<>();
		if (rule.exceptionRules.length > 0){
			//Create mappings for exception rules
			for (RuleDTO exceptionRule : rule.exceptionRules) {
				ArrayList<Mapping> fromExceptionMappings = getAllClasspathsOfModule(exceptionRule.moduleFrom, exceptionRule.violationTypeKeys);
				ArrayList<Mapping> toExceptionMappings = getAllClasspathsOfModule(exceptionRule.moduleTo, exceptionRule.violationTypeKeys);
				for (Mapping fromExceptionMapping : fromExceptionMappings) {
					for (Mapping toExceptionMapping : toExceptionMappings) {
						String fromToExceptionCombi = fromExceptionMapping.getPhysicalPath() + "|" + toExceptionMapping.getPhysicalPath();
						exceptionClassPathFromTos.add(fromToExceptionCombi);
					}
				}
			}
		}
		return exceptionClassPathFromTos;
	}

	// Used to create violations on: All relation rules + Facade convention 
	protected Violation createViolation(RuleDTO rule, DependencyDTO dependency, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);

		Severity violationTypeSeverity = getViolationTypeSeverity(dependency.type);
		Severity severity = getSeverity(configuration, this.severity, violationTypeSeverity);

		Violation newViolation = new Violation()
				.setRuletypeKey(this.key)
				.setLogicalModules(getLogicalModules(rule))
				.setClassPathFrom(dependency.from)
				.setClassPathTo(dependency.to)
				.setLineNumber(dependency.lineNumber)
				.setSeverity(severity.clone())
				.setViolationTypeKey(dependency.type)
				.setdependencySubType(dependency.subType)
				.setInDirect(dependency.isIndirect)
				.setIsInheritanceRelated(dependency.isInheritanceRelated)
				.setIsInnerClassRelated(dependency.isInnerClassRelated);
		return newViolation;
	}

	// Used to create violations on: Inheritance convention
	protected Violation createViolation(RuleDTO rule, Mapping classPathFrom, Mapping classPathTo, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Severity severity = getSeverity(configuration, this.severity, null);
		Violation newViolation = new Violation();
		newViolation = newViolation
				.setRuletypeKey(this.key)
				.setLogicalModules(getLogicalModules(rule))
				.setClassPathFrom(classPathFrom.getPhysicalPath())
				.setClassPathTo(classPathTo.getPhysicalPath())
				.setSeverity(severity.clone());
		return newViolation;
	}
	
	// Used to create violations on: Must use rule
	protected Violation createViolation(RuleDTO rule, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Severity severity = getSeverity(configuration, this.severity, null);
		Violation newViolation = new Violation();
		newViolation = newViolation
				.setRuletypeKey(this.key)
				.setLogicalModules(getLogicalModules(rule))
				.setSeverity(severity.clone());
		return newViolation;
	}

	// Used to create violations on: Naming convention
	protected Violation createViolation(RuleDTO rule, String classPathFrom, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		Severity severity = getSeverity(configuration, this.severity, null);
		Violation newViolation = new Violation();
		newViolation = newViolation
				.setRuletypeKey(this.key)
				.setLogicalModules(getLogicalModules(rule))
				.setClassPathFrom(classPathFrom)
				.setSeverity(severity.clone());
		return newViolation;
	}

	// Used to create violations on: Visibility convention
	protected Violation createViolation(RuleDTO rule, String classPathFrom, String violationTypeKey, ConfigurationServiceImpl configuration) {
		initializeViolationTypeFactory(configuration);
		final Severity violationTypeSeverity = getViolationTypeSeverity(violationTypeKey);
		Severity severity = getSeverity(configuration, this.severity, violationTypeSeverity);
		Violation newViolation = new Violation();
		newViolation = newViolation
				.setRuletypeKey(this.key)
				.setLogicalModules(getLogicalModules(rule))
				.setClassPathFrom(classPathFrom)
				.setSeverity(severity.clone())
				.setViolationTypeKey(violationTypeKey);
		return newViolation;
	}

	protected void initializeViolationTypeFactory(ConfigurationServiceImpl configuration) {
		if (violationTypeFactory == null) {
			this.violationTypeFactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
		}
	}

	protected Severity getViolationTypeSeverity(String violationTypeKey) {
		try {
			return violationTypeFactory.createViolationType(this.key, violationTypeKey).getSeverity();
		} catch (ViolationTypeNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Severity getSeverity(ConfigurationServiceImpl configuration, Severity ruleTypeSeverity, Severity violationTypeSeverity) {
		if (violationTypeSeverity == null && ruleTypeSeverity == null) {
			return null;
		}

		int ruleTypeValue = -1;
		int violationTypeValue = -1;

		if (ruleTypeSeverity != null) {
			ruleTypeValue = configuration.getSeverityValue(ruleTypeSeverity);
		}
		if (violationTypeSeverity != null) {
			violationTypeValue = configuration.getSeverityValue(violationTypeSeverity);
		}

		if (ruleTypeValue == -1 && violationTypeValue != -1) {
			return violationTypeSeverity;
		} else if (ruleTypeValue != -1 && violationTypeValue == -1) {
			return ruleTypeSeverity;
		} else if (ruleTypeValue != -1 && violationTypeValue != -1) {
			if (ruleTypeValue >= violationTypeValue) {
				return ruleTypeSeverity;
			} else {
				return violationTypeSeverity;
			}
		} else {
			return null;
		}
	}

	private LogicalModules getLogicalModules(RuleDTO rule) {
		LogicalModule logicalModuleFrom = new LogicalModule(rule.moduleFrom.logicalPath, "");
		LogicalModule logicalModuleTo = new LogicalModule(rule.moduleTo.logicalPath, ""); 
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
		return logicalModules;
	}
	
	public boolean equals(RuleTypes desiredRuleType) {
		return this.getKey().equals(desiredRuleType.toString());
	}
}