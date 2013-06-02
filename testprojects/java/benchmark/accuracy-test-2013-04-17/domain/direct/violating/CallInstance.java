package domain.direct.violating;

import domain.direct.Base;

public class CallInstance extends Base{
	
	public CallInstance(){
		
		System.out.println(profileDao.getCampaignType());
	}
}