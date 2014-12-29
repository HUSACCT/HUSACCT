package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClass extends Base{
	
	public CallConstructorInnerClass() {
		new CallInstanceInnerClassDAO();
	}
	
}
