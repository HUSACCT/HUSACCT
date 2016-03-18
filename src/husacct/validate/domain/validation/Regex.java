package husacct.validate.domain.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	public static String makeRegexString(String regexpath) {
		if (regexpath.startsWith("*") && !regexpath.endsWith("*")) {
			return regexpath.substring(1) + "$";
		} else if (regexpath.endsWith("*") && !regexpath.startsWith("*")) {
			return "^" + regexpath.substring(0,  regexpath.length() - 1);
		} else if (regexpath.startsWith("*") && regexpath.endsWith("*")) {
			return ".*" + regexpath.substring(1, regexpath.length() - 1) + ".*";
		} else {
			return "invalid";
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