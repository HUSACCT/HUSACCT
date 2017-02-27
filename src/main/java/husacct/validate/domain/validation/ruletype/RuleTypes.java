package husacct.validate.domain.validation.ruletype;

import husacct.validate.domain.validation.DefaultSeverities;

import java.util.EnumSet;

public enum RuleTypes {
	FACADE_CONVENTION("FacadeConvention", DefaultSeverities.HIGH),
	INHERITANCE_CONVENTION("InheritanceConvention", DefaultSeverities.MEDIUM),
	IS_NOT_ALLOWED_TO_USE("IsNotAllowedToUse", DefaultSeverities.HIGH),
	IS_NOT_ALLOWED_BACK_CALL("IsNotAllowedToMakeBackCall", DefaultSeverities.HIGH),
	IS_NOT_ALLOWED_SKIP_CALL("IsNotAllowedToMakeSkipCall", DefaultSeverities.HIGH),
	IS_ALLOWED_TO_USE("IsAllowedToUse", DefaultSeverities.LOW),
	IS_ONLY_ALLOWED_TO_USE("IsOnlyAllowedToUse", DefaultSeverities.HIGH),
	IS_THE_ONLY_MODULE_ALLOWED_TO_USE("IsTheOnlyModuleAllowedToUse", DefaultSeverities.HIGH),
	MUST_USE("MustUse", DefaultSeverities.MEDIUM),
	NAMING_CONVENTION("NamingConvention", DefaultSeverities.LOW),
	NAMING_CONVENTION_EXCEPTION("NamingConventionException", DefaultSeverities.MEDIUM),
	VISIBILITY_CONVENTION("VisibilityConvention", DefaultSeverities.MEDIUM),
	VISIBILITY_CONVENTION_EXCEPTION("VisibilityConventionException", DefaultSeverities.MEDIUM);
	
	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(
			FACADE_CONVENTION, INHERITANCE_CONVENTION, IS_NOT_ALLOWED_TO_USE, IS_NOT_ALLOWED_BACK_CALL, IS_NOT_ALLOWED_SKIP_CALL, IS_ONLY_ALLOWED_TO_USE,
			IS_THE_ONLY_MODULE_ALLOWED_TO_USE, MUST_USE, NAMING_CONVENTION, VISIBILITY_CONVENTION);

	private final String key;
	private final DefaultSeverities defaultSeverity;

	private RuleTypes(String key, DefaultSeverities defaultSeverity) {
		this.key = key;
		this.defaultSeverity = defaultSeverity;
	}

	public DefaultSeverities getDefaultSeverity() {
		return defaultSeverity;
	}

	public String toString() {
		return key;
	}
}