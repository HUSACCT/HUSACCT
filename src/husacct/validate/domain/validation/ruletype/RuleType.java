package husacct.validate.domain.validation.ruletype;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.violationtype.java.AbstractViolationType;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
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

	public abstract List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO appliedRule);

	protected Violation createViolation(DependencyDTO dependency, int severityValue, String ruleKey, LogicalModules logicalModules, boolean inDirect, Message message, Severity severity){
		return new Violation(dependency.lineNumber, severity, ruleKey, dependency.type, dependency.from, dependency.to, inDirect, message, logicalModules);
	}

	protected Violation createViolation(String ruleKey, String from, boolean inDirect, Message message, LogicalModules logicalModules, Severity severity){
		return new Violation(0, severity, ruleKey, "", from, "", inDirect, message, logicalModules);		
	}

	protected Severity getViolationTypeSeverity(String violationTypeKey){
		try{
			return violationtypefactory.createViolationType(violationTypeKey).getSeverity();
		}catch(ViolationTypeNotFoundException e){

		}
		return null;
	}
}