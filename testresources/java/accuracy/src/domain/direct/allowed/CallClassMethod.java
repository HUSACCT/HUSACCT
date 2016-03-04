package domain.direct.allowed;

import technology.direct.dao.BadgesDAO;

public class CallClassMethod {
	public CallClassMethod(){
	
		System.out.println(BadgesDAO.getAllBadges());
	}
}