package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class DeclarationTypeCast {
	
	public String getProfileInformation(){
		Object o = (ProfileDAO) new Object();
		return o.toString();
	}
}