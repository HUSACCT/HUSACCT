package domain.indirect.allowedfrom;

import domain.indirect.BaseIndirect;

public class CallInstanceMethodIndirect_SuperSuperClass extends BaseIndirect{
	
	public CallInstanceMethodIndirect_SuperSuperClass(){};
	
	public void MethodOfSuperClass() {
		subSubDao.MethodOnSuperClass(); 
	}
}
