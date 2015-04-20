package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class AccessObjectReferenceAsParameter{
	
	private ProfileDAO profileDao;
	private DeclarationParameter declaration;

	public AccessObjectReferenceAsParameter(){
		declaration.getProfileInformation(profileDao);
	}
}