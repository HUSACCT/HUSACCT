package husacct.validate.domain.validation.ruletype;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.rulefactory.RuletypesFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.List;

public abstract class RuleType {
	protected String key;
	protected String descriptionKey;
	protected String categoryKey;
	protected String exceptionKeys;
	protected List<RuleType> exceptionrules;
	protected List<ViolationType> violationtypes;

	protected RuletypesFactory ruletypelanguagefactory;

	public RuleType(String key, String categoryKey){
		this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
	}

	public RuleType(String key, String categoryKey, List<ViolationType> violationtypes){
		this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
		this.violationtypes = violationtypes;
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

	public List<ViolationType> getViolationTypes(){
		return violationtypes;
	}

	public abstract List<Violation> check(RuleDTO appliedRule);

	protected Violation createViolation(DependencyDTO dependency, int severityValue, String ruleKey, LogicalModules logicalModules, boolean inDirect, Message message){
		return new Violation(dependency.lineNumber, severityValue, ruleKey, dependency.type, dependency.from, dependency.to, inDirect, message, logicalModules);
	}
}