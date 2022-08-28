package domain.indirect.violatingfrom;

import domain.indirect.BaseIndirect;
import technology.direct.subclass.CallInstanceSubClassDOA; 

public class AccessInstanceVariableIndirect_SuperClass3 extends BaseIndirect{
	
	public AccessInstanceVariableIndirect_SuperClass(CallInstanceSubClassDOA subDao){
		this.subDao = subDao;
	}
}
