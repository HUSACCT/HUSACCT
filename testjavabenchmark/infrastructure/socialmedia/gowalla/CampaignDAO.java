package infrastructure.socialmedia.gowalla;

import domain.gowalla.Campaign;
//Functional requirement 3.2.1
//Test case 161: The classes in package infrastructure.socialmedia.gowalla are not allowed to use modules in a higher layer
//Result: FALSE
public class CampaignDAO {
	public CampaignDAO(){
		//FR5.1
		System.out.println(Campaign.getType());
	}
}