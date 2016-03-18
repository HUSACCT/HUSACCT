package domain.indirect.allowedfrom;

import domain.indirect.BaseIndirect;

public class AccessInstanceVariableIndirect_SuperClass extends BaseIndirect{
	
	public AccessInstanceVariableIndirect_SuperClass(){};
	
	public void Method() {
		String s = subDao.VariableOnSuperClass; 
		System.out.println(s);
	}
}
