package domain.facebook;

//Functional requirement 3.1.2
//Test case 82: Only class presentation.gui.facebook.PrivacySettingsGUI may have a dependency with domain.facebook.PrivacySettings
//Result: FALSE
public class PrivacySettings {
	public static String[] settings = {"no privacy", "block unknown"};
	
	public static String[] getPrivacySettings(){
		String[] returnStringArray = {"no privacy", "block unknown"};
		return returnStringArray;
	}
}