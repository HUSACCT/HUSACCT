package domain.direct.violating;

import domain.direct.Base;

public class CallInstanceSuperSuperClass extends Base{
	
	public CallInstanceSuperSuperClass(){};
	
	public void MethodOfSuperClass() {
		subSubDao.MethodOnSuperClass(); 
	}
}
