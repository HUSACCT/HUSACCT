package domain.direct.violating;

import technology.direct.dao.UserDAO;
public class AccessClassVariableConstant {
	public String sniName;
	public String sniMessage;
	
	public AccessClassVariableConstant(){
	}
	 
	public void testAccessStaticFinalAttribute() {
		sniName = UserDAO.name;
	}
}