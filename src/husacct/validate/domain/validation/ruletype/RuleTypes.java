package husacct.validate.domain.validation.ruletype;

import java.util.EnumSet;

public enum RuleTypes {
	IS_NOT_ALLOWED("IsNotAllowedToUse", 2),
	IS_ALLOWED("IsAllowedToUse", 2);

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(IS_NOT_ALLOWED, IS_ALLOWED);
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