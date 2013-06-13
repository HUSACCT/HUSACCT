package domain.direct.violating;

import domain.direct.Base;

public class AccessInstanceVariableWrite extends Base {
	
	public AccessInstanceVariableWrite(){

		String s = "profit";
		profileDao.name = s;
	}
}