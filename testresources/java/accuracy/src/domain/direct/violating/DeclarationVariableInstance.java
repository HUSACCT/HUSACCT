package domain.direct.violating;

import technology.direct.dao.ProfileDAO;
import technology.direct.dao.SettingsAnnotation;

public class DeclarationVariableInstance {
	
	@SuppressWarnings("unused") @SettingsAnnotation(title = "book")
	private ProfileDAO pdao;
	public String getProfileInformation(){
		return "";
	}
}