package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class DeclarationVariableLocal {
	
		public String getProfileInformation(){
			@SuppressWarnings("unused")
			ProfileDAO pdao;
		return "";
	}
}