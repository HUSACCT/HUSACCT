package domain.direct.violating;

import domain.direct.Base;

public class AccessObjectReferenceAsParameter extends Base{
	
	private DeclarationParameter declaration;

	public AccessObjectReferenceAsParameter(){
		declaration.getProfileInformation(profileDao);
	}
}