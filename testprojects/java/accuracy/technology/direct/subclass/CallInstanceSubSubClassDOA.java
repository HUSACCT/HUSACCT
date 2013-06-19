package technology.direct.subclass;

import technology.direct.dao.CallInstanceSuperClassDAO;

public class CallInstanceSubSubClassDOA extends CallInstanceSubClassDOA {
	
	public CallInstanceSubSubClassDOA() {
		};
	
	public CallInstanceSuperClassDAO MethodSuperClassInstance;
	
	public void test(){
		MethodSuperClassInstance.MethodOnSuperClass();
	}
}