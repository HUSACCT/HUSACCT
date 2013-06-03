package domain.direct.allowed;

import technology.direct.dao.CheckInDAO;

public class AccessClassVariable {
	public AccessClassVariable(){
	
		System.out.println(CheckInDAO.currentLocation);
	}
}