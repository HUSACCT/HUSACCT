package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.Collections;
import java.util.List;

public class MustUseRule extends RuleType{
	public MustUseRule(String key, String category, List<ViolationType> violationtypes) {
		super(key, category, violationtypes);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		//FIXME: 
		//TODO: implementation
		return Collections.emptyList();
	}
}
