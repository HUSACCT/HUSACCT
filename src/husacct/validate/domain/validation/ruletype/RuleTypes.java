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
	LOOPS_IN_MODULE("LoopsInModule", DefaultSeverities.HIGH),
	LOOPS_IN_MODULE_EXCEPTION("LoopsInModuleException", DefaultSeverities.MEDIUM);	

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(IS_NOT_ALLOWED, IS_ALLOWED, IS_ONLY_ALLOWED, IS_ONLY_MODULE_ALLOWED, MUST_USE, BACK_CALL, SKIP_CALL, NAMING_CONVENTION, VISIBILITY_CONVENTION, LOOPS_IN_MODULE);

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