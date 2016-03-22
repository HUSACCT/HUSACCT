package domain.indirect.allowedfrom;

import domain.indirect.BaseIndirect;

public class AccessInstanceVariableIndirect_SuperSuperClass extends BaseIndirect{
	
	public AccessInstanceVariableIndirect_SuperSuperClass(){};
	
	public void Method() {
		String s = subSubDao.VariableOnSuperClass; 
		System.out.println(s);
	}
}
