package domain.locationbased.latitude;

import infrastructure.socialmedia.locationbased.latitude.TipDAO;
//Functional requirement 3.2
//Test case 139: Class domain.locationbased.latitude.Tip is not allowed to use enumeration infrastructure.socialmedia.locationbased.foursqaure.TipDAO
//Result: TRUE
public class Tip {
	public Tip(){
		//FR5.2
		System.out.println(TipDAO.ONE);
	}
}