package husacct.validate.domain.validation.ruletype;

import husacct.validate.domain.validation.DefaultSeverities;

import java.util.EnumSet;

public enum RuleTypes {
	IS_NOT_ALLOWED("IsNotAllowedToUse", DefaultSeverities.MEDIUM),
	IS_ALLOWED("IsAllowedToUse", DefaultSeverities.LOW),
	IS_ONLY_ALLOWED("IsOnlyAllowedToUse", DefaultSeverities.LOW),
	IS_ONLY_MODULE_ALLOWED("IsOnlyModuleAllowedToUse", DefaultSeverities.MEDIUM),
	MUST_USE("MustUse", DefaultSeverities.MEDIUM),
	BACK_CALL("BackCall", DefaultSeverities.HIGH),
	SKIP_CALL("SkipCall", DefaultSeverities.LOW),

	NAMING_CONVENTION("NamingConvention", DefaultSeverities.MEDIUM),
	NAMING_CONVENTION_EXCEPTION("NamingConventionException", DefaultSeverities.MEDIUM),
	VISIBILITY_CONVENTION("VisibilityConvention", DefaultSeverities.MEDIUM),
	VISIBILITY_CONVENTION_EXCEPTION("VisibilityConventionException", DefaultSeverities.MEDIUM),
	INTERFACE_CONVENTION("InterfaceConvention", DefaultSeverities.LOW),
	SUBCLASS_CONVENTION("SubClassConvention", DefaultSeverities.MEDIUM),
	CYCLES_BETWEEN_MODULES("CyclesBetweenModules", DefaultSeverities.HIGH),
	CYCLES_BETWEEN_MODULES_EXCEPTION("CyclesBetweenModulesException", DefaultSeverities.MEDIUM);	

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(SUBCLASS_CONVENTION ,INTERFACE_CONVENTION ,IS_NOT_ALLOWED, IS_ONLY_ALLOWED, IS_ONLY_MODULE_ALLOWED, MUST_USE, BACK_CALL, SKIP_CALL, NAMING_CONVENTION, VISIBILITY_CONVENTION, CYCLES_BETWEEN_MODULES);

	private final String key;
	private final DefaultSeverities defaultSeverity;

	RuleTypes(String key, DefaultSeverities defaultSeverity){
		this.key = key;
		this.defaultSeverity = defaultSeverity;
	}

	public DefaultSeverities getDefaultSeverity() {
		return defaultSeverity;
	}

	@Override
	public String toString(){
		return key;
	}
}