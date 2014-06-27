package technology.direct.subclass;

import technology.direct.dao.CallInstanceSuperClassDAO;

public class CallInstanceSubClassDOA extends CallInstanceSuperClassDAO {
	
	public CallInstanceSubClassDOA() {
		};
	
	public CallInstanceSuperClassDAO MethodSuperClassInstance;
	
	public void test(){
		MethodSuperClassInstance.MethodOnSuperClass();
	}
}
