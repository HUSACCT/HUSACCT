package domain.direct.violating;

import java.util.ArrayList;

import domain.direct.Base;
import technology.direct.dao.ProfileDAO;

public class DeclarationReturnType_GenericType_OneTypeParameter extends Base{

	public ArrayList<ProfileDAO> testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(){
		return profileDAOs;
	}
}