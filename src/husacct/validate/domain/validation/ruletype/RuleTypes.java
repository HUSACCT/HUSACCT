package husacct.validate.domain.validation.ruletype;

import java.util.EnumSet;

public enum RuleTypes {
	IS_NOT_ALLOWED("IsNotAllowedToUse", 2),
	IS_ALLOWED("IsAllowedToUse", 2),
	IS_ONLY_ALLOWED("IsOnlyAllowedToUse", 2),
	IS_ONLY_MODULE_ALLOWED("IsOnlyModuleAllowedToUse",2),
	MUST_USE("MustUse",2);

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(IS_NOT_ALLOWED, IS_ALLOWED, IS_ONLY_ALLOWED,IS_ONLY_MODULE_ALLOWED,MUST_USE);
	public static final EnumSet<RuleTypes> allRuleTypes = EnumSet.allOf(RuleTypes.class);
	private final String key;
	private final int defaultSeverity;

	RuleTypes(String location, int defaultSeverity){
		this.key = location;
		this.defaultSeverity = defaultSeverity;
	}

	public int getDefaultSeverity(){
		return defaultSeverity;
	}

	@Override
	public String toString(){
		return key;
	}
}