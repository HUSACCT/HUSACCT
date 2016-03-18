package domain.direct.allowed;

import technology.direct.dao.ProfileDAO;

public class CallInstance{

	private ProfileDAO profileDao;

	public CallInstance(){
		
		System.out.println(profileDao.getCampaignType());
	}
}