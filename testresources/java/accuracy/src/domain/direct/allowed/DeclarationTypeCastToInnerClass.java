package domain.direct.allowed;

import import technology.direct.dao.CallInstanceOuterClassDAO;

public class DeclarationTypeCastToInnerClass {
	
	public String getProfileInformation(){
		Object o = (CallInstanceOuterClassDAO.CallInstanceInnerClassDAO) new Object();
		return o.toString();
	}
}