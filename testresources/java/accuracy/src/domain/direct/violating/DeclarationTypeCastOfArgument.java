package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class DeclarationTypeCastOfArgument {
	
	public String initializeProfileInformation(){
		String s;
		s = getProfileInformation((ProfileDAO) new Object());
		return s;
	}
	public String getProfileInformation(Object o){
		return o.toString();
	}
}