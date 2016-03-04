package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class DeclarationTypeCastToInnerClass {
	
	public String getProfileInformation(){
		Object o = (CallInstanceOuterClassDAO.CallInstanceInnerClassDAO) new Object();
		return o.toString();
	}
}