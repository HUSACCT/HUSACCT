package husacct.validate.domain.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	public static String makeRegexString(String regexpath) {
		if (regexpath.endsWith("**")) {
			return "^"
					+ regexpath.replaceAll("\\.", "\\\\.").replaceAll("\\*\\*",
							".*") + "$";
		} else if (regexpath.endsWith("*")) {
			return "^"
					+ regexpath.replaceAll("\\.", "\\\\.").replaceAll("\\*",
							"[a-zA-Z0-9]*") + "$";
		} else if (regexpath.startsWith("**")) {
			if (regexpath.endsWith("*")) {
				return "^"
						+ regexpath.substring(0, regexpath.length() - 1)
								.replaceAll("\\.", "\\\\.")
								.replaceAll("\\*\\*", ".*") + "[a-zA-Z0-9]*$";
			}
			return "^"
					+ regexpath.replaceAll("\\.", "\\\\.").replaceAll("\\*\\*",
							".*") + "$";
		} else if (regexpath.startsWith("*")) {
			if (regexpath.endsWith("*")) {
				return "^"
						+ regexpath.substring(0, regexpath.length() - 1)
								.replaceAll("\\.", "\\\\.")
								.replaceAll("\\*", "([a-zA-Z]*)(")
						+ ")([a-zA-Z0-9]*)$";
			}
			return "^"
					+ regexpath.replaceAll("\\.", "\\\\.").replaceAll("\\*",
							"([a-zA-Z0-9]*)(") + ")$";
		} else if (regexpath.endsWith("*")) {
			return regexpath.replaceAll("\\.", "\\\\.");
		} else {
			return "^" + regexpath.replaceAll("\\.", "\\\\.") + "$";
		}
	}

	public static boolean matchRegex(String regex, String value) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		while (matcher.find()) {
			return true;
		}
		return false;
	}
}
