package husacct.validate.domain.validation.ruletype;

import husacct.validate.domain.validation.DefaultSeverities;

import java.util.EnumSet;

public enum RuleTypes {
	IS_NOT_ALLOWED_TO_USE("IsNotAllowedToUse", DefaultSeverities.HIGH),
	IS_NOT_ALLOWED_BACK_CALL("IsNotAllowedToMakeBackCall", DefaultSeverities.HIGH),
	IS_NOT_ALLOWED_SKIP_CALL("IsNotAllowedToMakeSkipCall", DefaultSeverities.LOW),
	IS_ALLOWED_TO_USE("IsAllowedToUse", DefaultSeverities.LOW),
	IS_ONLY_ALLOWED_TO_USE("IsOnlyAllowedToUse", DefaultSeverities.LOW),
	IS_ONLY_MODULE_ALLOWED_TO_USE("IsOnlyModuleAllowedToUse", DefaultSeverities.MEDIUM),
	MUST_USE("MustUse", DefaultSeverities.MEDIUM),
	NAMING_CONVENTION("NamingConvention", DefaultSeverities.MEDIUM),
	NAMING_CONVENTION_EXCEPTION("NamingConventionException", DefaultSeverities.MEDIUM),
	VISIBILITY_CONVENTION("VisibilityConvention", DefaultSeverities.MEDIUM),
	VISIBILITY_CONVENTION_EXCEPTION("VisibilityConventionException", DefaultSeverities.MEDIUM),
	INTERFACEINHERITANCE_CONVENTION("InterfaceInheritanceConvention", DefaultSeverities.LOW),
	SUPERCLASSINHERITANCE_CONVENTION("SuperClassInheritanceConvention", DefaultSeverities.MEDIUM),
	FACADE_CONVENTION("FacadeConvention", DefaultSeverities.MEDIUM);
	
	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(
			FACADE_CONVENTION, SUPERCLASSINHERITANCE_CONVENTION, INTERFACEINHERITANCE_CONVENTION, IS_NOT_ALLOWED_TO_USE, IS_ONLY_ALLOWED_TO_USE,
			IS_ONLY_MODULE_ALLOWED_TO_USE, MUST_USE, IS_NOT_ALLOWED_BACK_CALL, IS_NOT_ALLOWED_SKIP_CALL, NAMING_CONVENTION, VISIBILITY_CONVENTION);
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