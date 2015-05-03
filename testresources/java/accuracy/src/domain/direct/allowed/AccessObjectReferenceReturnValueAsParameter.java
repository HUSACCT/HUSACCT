package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessObjectReferenceReturnValueAsParameter{
	
	private ProfileDAO profileDao;
	private DeclarationParameter declaration;

	public AccessObjectReferenceAsParameter(){
		declaration.getProfileInformation(getProfileDAO());
	}
	
	private ProfileDAO getProfileDAO() {
		return profileDao;
	}
}