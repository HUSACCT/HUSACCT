package domain.direct.violating;

import technology.direct.dao.CheckInDAO;

public class AccessClassVariable {
	public AccessClassVariable(){
	
		System.out.println(CheckInDAO.currentLocation);
	}
}