package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceVariableRead {
	
	private ProfileDAO profileDao;

	public AccessInstanceVariableRead(){
		
		System.out.println(profileDao.name);
	}
}