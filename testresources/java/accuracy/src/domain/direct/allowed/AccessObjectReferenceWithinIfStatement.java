package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class AccessObjectReferenceWithinIfStatement {
	
	private ProfileDAO profileDao;

	public AccessObjectReferenceWithinIfStatement(){
		if (profileDao != null) {
			String s = "wrong";
			System.out.println(s);
		}
	}
}