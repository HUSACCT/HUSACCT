package husacct.validate.domain.validation.ruletype.legalityofdependency;

import java.util.Collections;
import java.util.List;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

public class IsAllowedToUseRule extends RuleType {
	public IsAllowedToUseRule(String key, String category, List<ViolationType> violationtypes) {
		super(key, category, violationtypes);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		//Return no violations because is allowed to use is always true
		return Collections.emptyList();
	}
}