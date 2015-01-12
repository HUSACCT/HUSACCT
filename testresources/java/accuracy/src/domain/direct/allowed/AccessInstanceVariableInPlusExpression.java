package domain.direct.allowed;

import domain.direct.Base;

public class AccessInstanceVariableInPlusExpression extends Base {
	
	public AccessInstanceVariableRead(){
		
		System.out.println(profileDao.name + "test"); // Dependency at left side of +; right side is tested in CallInstanceInPlusExpression.
	}
}