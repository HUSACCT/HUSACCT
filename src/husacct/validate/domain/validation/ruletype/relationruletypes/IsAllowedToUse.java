package husacct.validate.domain.validation.ruletype.relationruletypes;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class IsAllowedToUse extends RuleType {

	public IsAllowedToUse(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.of(RuleTypes.IS_NOT_ALLOWED_TO_USE), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		// Return no violations because is allowed to use is always true
		return Collections.emptyList();
	}
}