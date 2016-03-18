package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class CallInstanceInPlusExpression {
	
	private ProfileDAO profileDao;

	public void CallInstance(){
		
		System.out.println("test" + profileDao.getCampaignType()); // Dependency at right side of +; left side is tested in AccessInstanceVariableRead.
	}
}