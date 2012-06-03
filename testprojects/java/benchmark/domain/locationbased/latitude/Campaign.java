package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.CampaignDAO;

//Functional requirement 3.2
//Test case 119: Class domain.locationbased.latitude.Campaign is not allowed to use class infrastructure.socialmedia.locationbased.foursquare.CampaignDAO 
//Result: TRUE
public class Campaign {
	//FR5.5
	private CampaignDAO dao;

	public Campaign(){
		//FR5.1
		System.out.println(dao.getCampaignType());
	}
}