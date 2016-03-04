package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceVariableInPlusExpression {
	
	private ProfileDAO profileDao;

	public void AccessInstanceVariableRead(){
		
		System.out.println(profileDao.name + "test"); // Dependency at left side of +; right side is tested in CallInstanceInPlusExpression.
	}
}