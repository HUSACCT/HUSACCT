package domain.indirect.violatingfrom;

import domain.indirect.BaseIndirect;
import technology.direct.subclass.CallInstanceSubClassDOA; 

public class AccessInstanceVariableIndirect_SuperClass2 extends BaseIndirect{
	
	public AccessInstanceVariableIndirect_SuperClass(CallInstanceSubClassDOA subDao){
		CallInstanceSubClassDOA sDao = new CallInstanceSubClassDOA();
		subDao = sDao;
	}
}
