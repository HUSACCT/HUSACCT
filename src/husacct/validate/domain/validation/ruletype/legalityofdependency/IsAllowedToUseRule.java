package husacct.validate.domain.validation.ruletype.legalityofdependency;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

public class IsAllowedToUseRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_NOT_ALLOWED);
	
	public IsAllowedToUseRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		//Return no violations because is allowed to use is always true
		return Collections.emptyList();
	}
}