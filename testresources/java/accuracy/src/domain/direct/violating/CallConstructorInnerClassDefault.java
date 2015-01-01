package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClassDefault extends Base{
	
	public CallConstructorInnerClass() {
		CallInstanceOuterClassDAO.CallInstanceInnerClassDAO v = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO();
	}
	
}
