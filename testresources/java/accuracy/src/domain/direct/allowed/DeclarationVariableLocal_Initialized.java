package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class DeclarationVariableLocal_Initialized {
	
		public String getProfileInformation(){
			@SuppressWarnings("unused")
			ProfileDAO pdao = null; 
		return ""; 
	}
}