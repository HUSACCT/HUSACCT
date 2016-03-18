package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceVariableWrite {
	
	private ProfileDAO profileDao;

	public AccessInstanceVariableWrite(){

		String s = "profit";
		profileDao.name = s;
	}
}