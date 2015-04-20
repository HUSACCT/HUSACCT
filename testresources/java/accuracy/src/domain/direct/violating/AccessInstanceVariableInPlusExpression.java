package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceVariableInPlusExpression {
	
	private ProfileDAO profileDao;

	public AccessInstanceVariableRead(){
		
		System.out.println(profileDao.name + "test"); // Dependency at left side of +; right side is tested in CallInstanceInPlusExpression.
	}
}