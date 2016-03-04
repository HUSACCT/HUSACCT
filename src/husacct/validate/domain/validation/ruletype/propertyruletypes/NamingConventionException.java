package husacct.validate.domain.validation.ruletype.propertyruletypes;

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

public class NamingConventionException extends RuleType {

	public NamingConventionException(String key, String category, List<ViolationType> violationTypes, Severity severity) {
		super(key, category, violationTypes, EnumSet.noneOf(RuleTypes.class), severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {
		return Collections.emptyList();
	}
}