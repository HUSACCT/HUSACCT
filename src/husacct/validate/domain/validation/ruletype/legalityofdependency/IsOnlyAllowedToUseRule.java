package husacct.validate.domain.validation.ruletype.legalityofdependency;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

public class IsOnlyAllowedToUseRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_NOT_ALLOWED);
		
	public IsOnlyAllowedToUseRule(String key, String category, List<ViolationType> violationtypes) {
		super(key, category, violationtypes, exceptionrules);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		//FIXME: 
		//TODO: implementation
		return Collections.emptyList();
	}
}