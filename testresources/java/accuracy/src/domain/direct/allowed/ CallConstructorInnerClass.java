package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClass extends Base{
	
	public CallConstructorInnerClass() {
		CallInstanceOuterClassDAO.CallInstanceInnerClassDAO v = new CallInstanceOuterClassDAO.CallInstanceInnerClassDAO("test");
	}
	
}
