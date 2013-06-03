package domain.direct.allowed;

import domain.direct.Base;

public class AccessObjectReferenceAsParameter extends Base{
	
	private DeclarationParameter declaration;

	public AccessObjectReferenceAsParameter(){
		declaration.getProfileInformation(profileDao);
	}
}