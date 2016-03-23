package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class DeclarationVariableInstance_MultipleAttributesAtTheSameLine {
	
	@SuppressWarnings("unused")
	private ProfileDAO p1Dao, p2Dao, p3Dao;

	public String getProfileInformation(){
		return "";
	}
}