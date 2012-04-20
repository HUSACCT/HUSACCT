package husacct.validate.domain.validation.ruletype.legalityofdependency;

import java.util.Collections;
import java.util.List;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

public class SkipCallRule extends RuleType {
	public SkipCallRule(String key, String category, List<ViolationType> violationtypes) {
		super(key, category, violationtypes);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		//FIXME: 
		//TODO: implementation
		return Collections.emptyList();
	}
}
