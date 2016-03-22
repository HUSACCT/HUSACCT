package domain.direct.allowed;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClass{
	
	public CallConstructorInnerClass() {
		CallInstanceOuterClassDAO.StaticNestedClass v = new CallInstanceOuterClassDAO.StaticNestedClass("test");
	}
	
}
