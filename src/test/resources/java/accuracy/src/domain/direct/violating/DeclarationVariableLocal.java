package domain.direct.violating;

import technology.direct.dao.ProfileDAO;
import technology.direct.dao.SettingsAnnotation;

public class DeclarationVariableLocal {
	
		public String getProfileInformation(){
			@SuppressWarnings("unused") @SettingsAnnotation(title = "book")
			ProfileDAO pdao;
		return "";
	}
}