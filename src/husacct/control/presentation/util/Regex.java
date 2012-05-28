package husacct.control.presentation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	
	public static String nameRegex = "^[a-zA-Z0-9-_]*$";
	public static String filenameRegex = "^[a-zA-Z0-9-_]+\\.?[a-zA-Z0-9]+$";
	
	public static boolean matchRegex(String regex, String value){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		while (matcher.find()) {
			return true;
		}
		return false;
	}
}
