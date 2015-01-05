package domain.direct.allowed;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClass{
	
	public CallConstructorInnerClass() {
		CallInstanceOuterClassDAO.CallInstanceInnerClassDAO v = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO("test");
	}
	
}
