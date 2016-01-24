package domain.direct.violating;

import java.util.ArrayList;

import technology.direct.dao.ProfileDAO;

public class DeclarationReturnType_GenericType_OneTypeParameter extends Base{

	public ArrayList<ProfileDAO> testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		return profileDAOs;
	}
}