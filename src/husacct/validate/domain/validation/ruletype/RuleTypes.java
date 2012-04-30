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
	
	INTERFACE_CONVENTION("InterfaceConvention", DefaultSeverities.MEDIUM),
	SUBCLASS_CONVENTION("SubClassConvention", DefaultSeverities.MEDIUM);

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(IS_NOT_ALLOWED, IS_ALLOWED, IS_ONLY_ALLOWED,IS_ONLY_MODULE_ALLOWED,MUST_USE,BACK_CALL,SKIP_CALL,INTERFACE_CONVENTION, SUBCLASS_CONVENTION);
	//public static final EnumSet<RuleTypes> allRuleTypes = EnumSet.allOf(RuleTypes.class);
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