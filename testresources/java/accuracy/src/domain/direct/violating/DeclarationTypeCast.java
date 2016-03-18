package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class DeclarationTypeCast {
	
	public String getProfileInformation(){
		Object o = (ProfileDAO) new Object();
		return o.toString();
	}
}