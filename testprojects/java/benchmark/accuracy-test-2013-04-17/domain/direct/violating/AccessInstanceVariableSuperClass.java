package domain.direct.violating;

import domain.direct.Base;

public class AccessInstanceVariableSuperClass extends Base{
	
	public AccessInstanceVariableSuperClass(){};
	
	public void Method() {
		String s = subDao.VariableOnSuperClass; 
		System.out.println(s);
	}
}
