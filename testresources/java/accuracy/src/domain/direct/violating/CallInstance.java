package domain.direct.violating;

import technology.direct.dao.ProfileDAO;

public class CallInstance{

	private ProfileDAO profileDao;

	public CallInstance(){
		
		System.out.println(profileDao.getCampaignType());
	}
}