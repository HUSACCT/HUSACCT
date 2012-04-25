package husacct.validate.domain.validation.ruletype;

import java.util.EnumSet;

public enum RuleTypes {
	IS_NOT_ALLOWED("IsNotAllowedToUse", "Low"),
	IS_ALLOWED("IsAllowedToUse", "Low"),
	IS_ONLY_ALLOWED("IsOnlyAllowedToUse", "Low"),
	IS_ONLY_MODULE_ALLOWED("IsOnlyModuleAllowedToUse", "Low"),
	MUST_USE("MustUse", "Low"),
	BACK_CALL("BackCall", "Low"),
	SKIP_CALL("SkipCall", "Low");

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(IS_NOT_ALLOWED, IS_ALLOWED, IS_ONLY_ALLOWED,IS_ONLY_MODULE_ALLOWED,MUST_USE,BACK_CALL,SKIP_CALL);
	public static final EnumSet<RuleTypes> allRuleTypes = EnumSet.allOf(RuleTypes.class);
	private final String key;
	private final String defaultSeverityKey;

	RuleTypes(String key, String defaultSeverityKey){
		this.key = key;
		this.defaultSeverityKey = defaultSeverityKey;
	}

	public String getDefaultSeverityKey(){
		return defaultSeverityKey;
	}

	@Override
	public String toString(){
		return key;
	}
}