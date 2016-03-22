package domain.direct.violating;

import technology.direct.subclass.CallInstanceSubClassDOA;

public class AccessInstanceVariableSuperClass {
	
	private CallInstanceSubClassDOA subDao;
	
	public AccessInstanceVariableSuperClass(){};
	
	public void Method() {
		String s = subDao.VariableOnSuperClass; 
		System.out.println(s);
	}
}
