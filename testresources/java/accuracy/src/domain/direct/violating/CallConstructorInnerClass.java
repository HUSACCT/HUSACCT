package domain.direct.violating;

import technology.direct.dao.CallInstanceOuterClassDAO;

public class CallConstructorInnerClass{
	
	public CallConstructorInnerClass() {
		CallInstanceOuterClassDAO.StaticNestedClass v = new CallInstanceOuterClassDAO.StaticNestedClass("test");
	}
	
}
