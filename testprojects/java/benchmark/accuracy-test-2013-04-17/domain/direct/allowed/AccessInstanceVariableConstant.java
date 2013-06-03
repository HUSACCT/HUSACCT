package domain.direct.allowed;

import domain.direct.Base;

public class AccessInstanceVariableConstant extends Base {
	public String sniName;
	public String sniMessage;
	
	public AccessInstanceVariableConstant(){
	}
	 
	public void testAccessFinalAttribute() {
		sniMessage = userDao.message;
	}
	
}