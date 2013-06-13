package domain.direct.allowed;

import domain.direct.Base;

public class CallInstanceSuperSuperClass extends Base{
	
	public CallInstanceSuperSuperClass(){};
	
	public void MethodOfSuperClass() {
		subSubDao.MethodOnSuperClass(); 
	}
}
