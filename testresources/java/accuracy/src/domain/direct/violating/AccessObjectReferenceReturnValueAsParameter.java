package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class AccessObjectReferenceReturnValueAsParameter{
	
	private ProfileDAO profileDao;
	private DeclarationParameter declaration;

	public void AccessObjectReferenceAsParameter(){
		declaration.getProfileInformation(getProfileDAO());
	}
	
	private ProfileDAO getProfileDAO() {
		return profileDao;
	}
}