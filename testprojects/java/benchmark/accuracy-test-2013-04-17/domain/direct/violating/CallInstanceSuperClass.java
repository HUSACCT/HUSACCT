package domain.direct.violating;

import domain.direct.Base;

public class CallInstanceSuperClass extends Base{
	
	public CallInstanceSuperClass(){};
	
	public void MethodOfSuperClass() {
		subDao.MethodOnSuperClass(); 
	}
}
