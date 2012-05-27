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
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.EnumSet;
import java.util.List;

public abstract class RuleType {
	protected final String key;
	protected final String descriptionKey;
	protected final String categoryKey;
	protected final EnumSet<RuleTypes> exceptionRuleKeys;	
	protected final List<ViolationType> violationtypes;	
	protected List<RuleType> exceptionrules;
	protected final Severity severity;

	protected AbstractViolationType violationtypefactory;
	protected List<Violation> violations;
	protected Mappings mappings;
	protected List<Mapping> physicalClasspathsFrom;

	protected final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
	protected final IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	public RuleType(String key, String categoryKey, List<ViolationType> violationtypes, EnumSet<RuleTypes> exceptionRuletypes, Severity severity){
		this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
		this.violationtypes = violationtypes;
		this.exceptionRuleKeys = exceptionRuletypes;
		this.severity = severity;
	}

	public String getKey(){
		return key;
	}

	public String getDescriptionKey(){
		return descriptionKey;
	}

	public String getCategoryKey(){
		return categoryKey;
	}

	public EnumSet<RuleTypes> getExceptionRuleKeys(){
		return exceptionRuleKeys;
	}

	public List<ViolationType> getViolationTypes(){
		return violationtypes;
	}

	public void setExceptionrules(List<RuleType> ruletypes){
		this.exceptionrules = ruletypes;
	}

	public List<RuleType> getExceptionrules(){
		return exceptionrules;
	}

	public Severity getSeverity(){
		return severity;
	}

	public abstract List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule);

	protected Severity getViolationTypeSeverity(String violationTypeKey){
		try{
			return violationtypefactory.createViolationType(violationTypeKey).getSeverity();
		}catch(ViolationTypeNotFoundException e){

		}
		return null;
	}

	protected Violation createViolation(RuleDTO rootRule,Mapping classPathFrom, Mapping classPathTo,DependencyDTO dependency, ConfigurationServiceImpl configuration){
		Message message = new Message(rootRule);
		LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
		LogicalModules logicalModules;
		Violation violation;

		if(classPathTo == null){
			logicalModules = new LogicalModules(logicalModuleFrom);
		}else{
			LogicalModule logicalModuleTo = new LogicalModule(classPathTo);
			logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
		}
		if(dependency == null){

			Severity severity = CheckConformanceUtilSeverity.getSeverity(configuration, this.severity, null);
			violation = new Violation(0, severity.clone(), this.key, "", classPathFrom.getPhysicalPath(), "", false, message, logicalModules);
		}else{

			final Severity violationTypeSeverity = getViolationTypeSeverity(dependency.type);
			Severity severity = CheckConformanceUtilSeverity.getSeverity(configuration, this.severity, violationTypeSeverity);						
			violation = new Violation(dependency.lineNumber, severity.clone(), this.key, dependency.type, dependency.from, dependency.to, false, message, logicalModules);
		}
		return violation;

	}
}