package domain.direct.allowed;

import technology.direct.dao.ISierraDAO;
public class AccessClassVariableInterface {
	public String sniName;
	
	public AccessClassVariableInterface(){
	}
	 
	public void testAccessFinalAttribute() {
		sniName = ISierraDAO.NAME;
	}
}