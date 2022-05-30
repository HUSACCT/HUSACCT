package domain.indirect.violatingfrom;

import domain.indirect.BaseIndirect;

public class AccessInstanceVariableIndirect_SuperClass2 extends BaseIndirect{
	
	public AccessInstanceVariableIndirect_SuperClass(){}
	
	public void Method2(int subDao) {
		int i = 1;
		i = subDao;
	}
}
