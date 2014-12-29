package domain.direct.allowed;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClass extends Base{
	
	public CallConstructorInnerClass() {
		new CallInstanceInnerClassDAO();
	}
	
}
