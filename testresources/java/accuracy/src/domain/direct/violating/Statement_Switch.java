package domain.direct.violating;

import technology.direct.dao.ProfileDAO;
import technology.direct.dao.CheckInDAO;
import technology.direct.dao.UserDAO;

public class Statement_Switch{

	private ProfileDAO profileDao;
	private CheckInDAO checkInDAO;
	private UserDAO userDAO;

	public Statement_Switch(){
		String campaignType = "";
		switch (userDAO.message) {
		case profileDao.getCampaignType():
			campaignType = profileDao.name;
			break;
		case "":
			campaignType = "no";	
			break;
		case checkInDAO.currentLocation:
			campaignType = "yes";	
			break;
		default:
			campaignType = checkInDAO.name;
		}
	}
}