package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClassDefault{
	
	public CallConstructorInnerClass() {
		CallInstanceOuterClassDAO.CallInstanceInnerClassDAO v = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
	}
	
}
