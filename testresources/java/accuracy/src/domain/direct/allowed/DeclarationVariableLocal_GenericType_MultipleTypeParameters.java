package domain.direct.allowed;

import java.util.HashMap;
import technology.direct.dao.FriendsDAO;
import technology.direct.dao.ProfileDAO;

public class DeclarationVariableLocal_GenericType_MultipleTypeParameters {
	
		public String getProfileInformation(){
			@SuppressWarnings("unused")
			HashMap<ProfileDAO, FriendsDAO> hashMap;
		return "";
	}
}