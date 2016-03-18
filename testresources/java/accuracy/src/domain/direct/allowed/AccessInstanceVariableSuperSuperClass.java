package domain.direct.allowed;

import domain.direct.Base;

public class AccessInstanceVariableSuperSuperClass extends Base{
	
	public AccessInstanceVariableSuperSuperClass(){};
	
	public void Method() {
		String s = subSubDao.VariableOnSuperClass; 
		System.out.println(s);
	}
}
