package husacct.validate.domain.ruletype;

import java.util.EnumSet;

public enum RuleTypes {
	IS_NOT_ALLOWED("IsNotAllowedToUse");

	public static final EnumSet<RuleTypes> mainRuleTypes = EnumSet.of(IS_NOT_ALLOWED);
	public static final EnumSet<RuleTypes> allRuleTypes = EnumSet.allOf(RuleTypes.class);
	private final String key;

	RuleTypes(String location){
		this.key = location;
	}

	@Override
	public String toString(){
		return key;
	}
}