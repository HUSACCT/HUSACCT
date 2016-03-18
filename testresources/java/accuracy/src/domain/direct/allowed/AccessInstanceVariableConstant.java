package domain.direct.allowed;

import technology.direct.dao.UserDAO;

public class AccessInstanceVariableConstant {
	private UserDAO userDao;
	public String sniName;
	public String sniMessage;
	
	public AccessInstanceVariableConstant(){
	}
	 
	public void testAccessFinalAttribute() {
		sniMessage = userDao.message;
	}
	
}