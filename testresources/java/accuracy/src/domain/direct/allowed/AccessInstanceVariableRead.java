package domain.direct.allowed;

import domain.direct.Base;

public class AccessInstanceVariableRead extends Base {
	
	public AccessInstanceVariableRead(){
		
		System.out.println(profileDao.name);
	}
}