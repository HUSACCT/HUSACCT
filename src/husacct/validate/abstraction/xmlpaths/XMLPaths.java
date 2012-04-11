package husacct.validate.abstraction.xmlpaths;

public abstract class XMLPaths {
	
	public static final String HOME = System.getProperty("user.home") + "/HUSACCT";
	public static final String VIOLATIONS_DIR = HOME + "/violations";
	public static final String VIOLATIONS_PATH = HOME + "/violations/violations.xml";
	public static final String SEVERITIES_PAT = "configuration/severities.xml";

}
