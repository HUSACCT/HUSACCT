package husacct.validate.task;

import husacct.validate.abstraction.language.ResourceBundles;

public class MessageBuilder {
	public static String getRuleMessage(String from, String fromType, String to, String toType, String ruleTypeKey) {
		StringBuilder builder = new StringBuilder();
		builder.append(fromType + " " + from + " ");
		builder.append(ResourceBundles.getValue(ruleTypeKey) + " ");
		builder.append(toType + " " + to);
		return builder.toString();
	}
}