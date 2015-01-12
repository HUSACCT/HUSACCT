package domain.direct.violating;

import domain.direct.Base;

public class CallInstanceInPlusExpression extends Base{
	
	public CallInstance(){
		
		System.out.println("test" + profileDao.getCampaignType()); // Dependency at right side of +; left side is tested in AccessInstanceVariableRead.
	}
}