package domain.direct.violating;

import java.util.ArrayList;
import technology.direct.dao.ProfileDAO;

public class DeclarationParameter_GenericType_OneTypeParameter {
	
	public String testUsageOfInstanceVariableDefinedByGenericTypeOneParameterType(ArrayList<ProfileDAO> pDao){
		return pDao.get(0).toString();
	}
}